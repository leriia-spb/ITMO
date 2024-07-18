'use strict'
const index = {'x': 0, 'y': 1, 'z': 2}
const cnst = a => () => a
const variable = s => (...args) => args[index[s]]
const operation = func => (...args) => (...vars) => func(...args.map((x) => x(...vars)))
const add = operation((a, b) => a + b)
const subtract = operation((a, b) => a - b)
const multiply = operation((a, b) => a * b)
const divide = operation((a, b) => a / b)
const negate = operation((a) => -a)
const avg5 = operation((a, b, c, d, e) => (a + b + c + d + e) / 5)
const med3 = operation((a, b, c) => [a, b, c].sort((a, b) => b - a)[1])

const operations = {'+': add, '-': subtract, '*': multiply, '/': divide, 'negate': negate, 'avg5': avg5, 'med3': med3}
const count_of_operands = {"+": 2, "-": 2, "*": 2, "/": 2, 'negate': 1, 'avg5': 5, 'med3': 3}
const pi = cnst(Math.PI)
const e = cnst(Math.E)
const consts = {'pi': pi, 'e': e}

const parse = function (string) {
    const mas = string.trim().split(/\s+/); //.filter((str) => str.length > 0);
    const stack = []
    for (const item of mas) {
        if (item in index) {
            stack.push(variable(item))
        } else if (item in operations) {
            stack.push(operations[item](...stack.splice(stack.length - count_of_operands[item])))
        } else if (item in consts) {
            stack.push(consts[item])
        } else {
            stack.push(cnst(parseFloat(item)))
        }
    }
    return stack[0]
}
