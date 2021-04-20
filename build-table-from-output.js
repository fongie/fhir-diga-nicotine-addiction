#!/usr/bin/env node
const fs = require('fs')

// This converts one structure definition or code system into a latex booktabs table

const help = 'Missing arguments. Pass the input FHIR resource json file with -f {filename} and the target tex file output path with -o {path}. Optionally, you can choose the target implementation guide path with -i.'
let args = process.argv
if (args.length < 3) {
    console.error(help)
    return
}

let target;
let file;
let igFile = './fhir-profile/fsh-generated/resources/ImplementationGuide-com.alextherapeutics.fhir.nicotine.json'
const available = ['-h', '-f', '-o', '-i']
args.shift() // remove the first node args
args.shift()
while (args.length > 0) {
    const operator = args.shift()
    if (!available.includes(operator)) {
        console.error(help)
        return
    }
    if (operator === '-h') {
        console.info(help)
        return
    }
    const arg = args.shift()
    if (operator === '-o') {
        target = arg
    } else if (operator === '-f') {
        file = arg
    } else if (operator === '-i') {
        igFile = arg
    } else {
        console.error('Internal error. Shouldnt arrive here')
        return
    }
}
if (!target || !file) {
    console.error('You must specify an output path and file target. See -h for help')
    return
}

console.info(`Using the FHIR file ${file} to generate a LaTeX booktabs table to ${target}...`)
const json = JSON.parse(fs.readFileSync(file))
const igJson = JSON.parse(fs.readFileSync(igFile))

const result = json.resourceType === 'CodeSystem'
    ? buildTableFromCodeSystem(json, igJson)
    : buildTableFromStructureDefinition(json, igJson)

fs.writeFileSync(target, result)
console.info('Done')

// ---------------------------------------------------------------------------------------------------------
// functions

function escape(line) {
    return line.replace(/\_/g, '\\_')
}

function buildTableFromCodeSystem(json, igJson) {
    const name = json.name
    const bookTabsHeader =
        `
\\begin{table}[]\\centering
\\begin{tabular}{@{}ll@{}}
\\toprule
\\multicolumn{1}{c}{code}               & \\multicolumn{1}{c}{definition}       \\\\ \\midrule
`
    const structureHeader = `
\\textbf{${name}} & \\textbf{${json.description}}  \\\\ \\midrule
`
    const rows = json.concept.map(item => `${escape(item.code)} & ${item.definition} \\\\`)
    const elements = rows.join('\n').concat('\\bottomrule')
    const bookTabsEnd = `
\\end{tabular}
\\caption{The ${name} code system.}
\\label{tab:${name}}
\\end{table}
`
    return bookTabsHeader + structureHeader + elements + bookTabsEnd
}

function buildTableFromStructureDefinition(json, igJson) {

    const uri = json.url
    const name = json.name
    const type = json.type
    const kind = json.kind
    const caption = kind === 'resource'
    ? `The differential for the ${name} profile when compared to the base ${type} resource.`
    : `The differential for the ${name} extension.`

    const bookTabsHeader =
        `
\\begin{table}[]\\centering
\\begin{tabular}{@{}lll@{}}
\\toprule
\\multicolumn{1}{c}{Name}               & \\multicolumn{1}{c}{card.} & \\multicolumn{1}{c}{Type}       \\\\ \\midrule
`

    const structureHeader = `
\\textbf{${name}} & \\textbf{-}                & \\textbf{${type}} \\\\ \\midrule
`

    const jsonDifferential = json.differential.element
    jsonDifferential.shift() // first element is the parent
    const jsonSnapshot = json.snapshot.element

    const tabRows = jsonDifferential.map(item => buildTabRow(item, jsonSnapshot, type))
    const elements = tabRows.join('\n').concat('\\bottomrule')
    const bookTabsEnd = `
\\end{tabular}
\\caption{${caption}}
\\label{tab:${name}}
\\end{table}
`
    return bookTabsHeader + structureHeader + elements + bookTabsEnd
}
function buildTabRow(item, snapshots, type) {
    const rowName = getRowName(item)
    const cardMin = item.min !== undefined ? item.min : snapshots.find(el => el.id === item.id).min //|| 0
    const cardMax = item.max || snapshots.find(el => el.id === item.id).max
    const rowType = getType(item, snapshots, type)
    if (rowType === undefined) {
        console.warn(`${rowName} had undefined type. ignoring`)
        return ''
    }
    return `${rowName} & ${cardMin}..${cardMax} & ${rowType} \\\\`
}

function getRowName(item) {
    const nameWithPath = item.id.substring(item.id.indexOf('.') + 1)
    const formatted = nameWithPath.replace(/\./g, ' $\\rightarrow$ ').replace(/:/g, ' $\\mid$ ')
    return formatted
}

function getType(item, snapshots, type) {
    if (item.fixedUri) {
        return `uri=${item.fixedUri}`
    }
    if (item.id.includes('valueDate')) {
        return 'date'
    }
    if (item.id.includes('valueCodeableConcept')) {
        return 'CodeableConcept'
    }
    if (item.binding) {
        const valueSetId = findIdFromCanonical(item.binding.valueSet)
        const valueSetName = findProfileNameFromIg(igJson, valueSetId)
        return `${valueSetName} (${item.binding.strength})`
    }
    const types = item.type || snapshots.find(el => el.id === item.id).type
    let typeString
    if (types.length === 1) {
        typeString = buildTypeString(types[0], snapshots)
    } else {
        typeString = type.code
    }
    let pattern
    if (item.patternString) {
        pattern = item.patternString
    } else if (item.patternCode) {
        pattern = item.patternCode
    }
    return pattern !== undefined ? `${typeString} = ${pattern}` : typeString
}

function buildTypeString(type, snapshots) {
    if (!type.targetProfile) {
        return type.code
    }
    const targetProfileId = findIdFromCanonical(type.targetProfile[0])
    const name = findProfileNameFromIg(igJson, targetProfileId)
    return `${type.code}(${name})`
}

function findProfileNameFromIg(ig, targetId) {
    if (!targetId.includes('-')) return targetId
    const def = igJson.definition.resource.find(el => el.reference.reference.includes(targetId))
    if (def.exampleCanonical) {
        return findProfileNameFromIg(findIdFromCanonical(def.exampleCanonical))
    }
    return def.name.replace(/\s/g, '')
}

function findIdFromCanonical(canonical) {
    const split = canonical.split('/')
    return split[split.length - 1]
}
