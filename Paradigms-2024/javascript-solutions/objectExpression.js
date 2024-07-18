'use strict'
const indexOfVars = {'x':0, 'y':1, 'z':2}
const operations = {'+': Add, '-': Subtract, '*': Multiply, '/': Divide, 'negate': Negate, 'hypot': Hypot, 'hmean': HMean, 'arithMean': ArithMean, 'geomMean': GeomMean, 'harmMean': HarmMean}
const count_of_operands = {"+": 2, "-": 2, "*":2, "/":2, 'negate':1, 'hypot': 2, 'hmean': 2, 'arithMean': 0, 'geomMean': 0, 'harmMean': 0}

const constTwo = new Const(2)
const constOne = new Const(1)
const constZero = new Const(0)

function ParseError(message) {
    this.message = message;
}
ParseError.prototype = Object.create(Error.prototype);
ParseError.prototype.name = "ParseError";
ParseError.prototype.constructor = ParseError;

function Constructor(Parent, Child, str, func, diff, ...args) {
    Parent.call(this, str, func, diff, ...args);
    Child.prototype = Object.create(Parent.prototype);
    Child.prototype.constructor = Child;
}

function PrimitiveType(str, func, diff) {
    this.toString = () => str.toString()
    this.prefix = () => str.toString()
    this.postfix = () => str.toString()
    this.evaluate = func
    this.diff = diff
}
function Const(num) {
    Constructor.call(this, PrimitiveType, Const,  num, ()=>num, ()=>constZero);
}
function Variable(name) {
    Constructor.call(this, PrimitiveType, Variable,  name, (...args) => args[indexOfVars[name]], (str) => str === name ? constOne : constZero);
}
function Operation(name, func, diff, ...args) {
    this.toString = () => args.map((x) => x.toString()).join(' ') + " " + name
    this.prefix = () => '(' + name + ' ' + args.map((x) => x.prefix()).join(' ') + ')'
    this.postfix = () => '(' + args.map((x) => x.postfix()).join(' ') + ' ' + name + ')'
    this.evaluate = (...vars) => func(...args.map((x) => x.evaluate(...vars)))
    this.diff = diff
}
function Add (arg1, arg2){
    Constructor.call(this, Operation, Add, '+', (a,b) => a+b, (str) =>
    new Add(arg1.diff(str), arg2.diff(str)), arg1, arg2);
}
function Subtract (arg1, arg2){
    Constructor.call(this, Operation, Subtract, '-', (a,b)=>a-b, (str) =>
    new Subtract(arg1.diff(str), arg2.diff(str)), arg1, arg2);
}
function Multiply (arg1, arg2){
    Constructor.call(this, Operation, Multiply, '*', (a,b)=>a*b, (str) =>
    new Add(
        new Multiply(arg1.diff(str), arg2),
        new Multiply(arg2.diff(str), arg1)), arg1, arg2);
}
function Divide (arg1, arg2){
    Constructor.call(this, Operation, Divide, '/', (a,b)=>a/b, (str) =>
    new Divide(
        new Subtract(
            new Multiply(arg1.diff(str), arg2),
            new Multiply(arg1, arg2.diff(str))),
        new Multiply(arg2, arg2)), arg1, arg2);
}
function Negate (arg){
    Constructor.call(this, Operation, Negate, 'negate', (a)=>-a,(str) =>
    new Negate(arg.diff(str)), arg);
}
function Hypot(arg1, arg2){
     Constructor.call(this, Operation, Hypot, 'hypot', (a, b) => a*a + b*b, (str) =>
     new Multiply(
        constTwo,
        new Add(
            new Multiply(arg1, arg1.diff(str)),
            new Multiply(arg2, arg2.diff(str)))), arg1, arg2);
}
function HMean(arg1, arg2){
     Constructor.call(this, Operation, HMean, 'hmean', (a, b) => 2 / (1 / a + 1 / b), (str) =>
     new Multiply(
         constTwo,
         new Divide(
             new Add(
                new Multiply(
                    new Multiply(arg1, arg1),
                    arg2.diff(str)),
                new Multiply(
                    new Multiply(arg2, arg2),
                    arg1.diff(str))),
             new Multiply(
                new Add(arg1, arg2),
                new Add(arg1, arg2)))), arg1, arg2);
}
function ArithMean(...args){
     Constructor.call(this, Operation, ArithMean, 'arithMean',
     (...arg) => arg.reduce((a, b) => a+b,0,) / arg.length,
     (str) => new Divide(
         args.reduce((a, b) => new Add(a, b.diff(str)), constZero), new Const(
         args.length)), ...args)
}
function GeomMean(...args){
     Constructor.call(this, Operation, GeomMean, 'geomMean',
     (...args) => Math.pow(Math.abs(args.reduce((a, b) => a*b,1,)), 1 / args.length),
     (str) =>{
         const geom = new GeomMean(... args)
         let geoms = constOne
         for(let i = 1; i < args.length; i++){
             geoms = new Multiply(geoms, geom)
         }
     return new Divide(
        args.reduce((a, b) => new Multiply(a, b), constOne,).diff(str),
        new Multiply(
            new Const(args.length),
             geoms))
     }, ...args)
}
function HarmMean(...args){
     Constructor.call(this, Operation, HarmMean, 'harmMean',
     (...args) => args.length / args.reduce((a, b) => a+1/b,0,),
     (str) => new Divide(
         new Const(args.length),
         args.reduce((a, b) => new Add(a, new Divide(constOne, b)),constZero,)
         ).diff(str), ...args)
}

function Parser(mas, reverse) {
    this.mas = (reverse) ? mas.replaceAll(/\(|\)/g, (a) => ' ' + a + ' ').split(/\s/).filter((str) => str.length > 0).reverse(): mas.replaceAll(/\(|\)/g, (a) => ' ' + a + ' ').split(/\s/).filter((str) => str.length > 0);
    this.start = (reverse) ? ")": '('
    this.end = (reverse) ? "(": ')'
    this.parse = (startIndex) => {
        let index = startIndex
        if(this.mas[index] === this.start){
            index++
            let item = this.mas[index]
            const stack = []
            while(index < mas.length && item !== this.end) {
                if (item === this.start){
                    const res = this.parse(index)
                    stack.push(res[0])
                    index = res[1]
                }
                else if (item in indexOfVars)
                    stack.push(new Variable(item))
                else if (item in operations){
                    if (stack.length < count_of_operands[item]){
                        throw new ParseError('Too few arguments')
                    }
                    let miniStack = stack.splice(stack.length - count_of_operands[item])
                    if (count_of_operands[item] === 0){
                        miniStack = stack.splice(0)
                    }
                    stack.push(new operations[item](...(reverse) ? miniStack.reverse(): miniStack))
                    item = this.mas[++index]
                    break
                }
                else if (!Number.isNaN(+item)){
                    stack.push(new Const(+item))
                }
                else{
                    throw new ParseError('Unexpected token ' + this.mas[index])
                }
            item = this.mas[++index]
            }
            if (item !== this.end){
                throw new ParseError("Missing '" + this.end + "'")
            }
            if (stack.length > 1){
                 throw new ParseError('Too many arguments')
            }
            if (stack.length < 1){
                 throw new ParseError('Too few arguments')
            }
            if (startIndex === 0 && index + 1 !== this.mas.length){
                throw new ParseError('Expected end of expression')
            }
            if (stack[0] instanceof Operation){
                return [stack[0], index]
            }
            throw new ParseError('Expected operation in brackets')
        }
        else if (this.mas[index] in indexOfVars && this.mas.length === 1)
            return [new Variable(this.mas[index]), 1]
        else if (!Number.isNaN(+this.mas[index]) && this.mas.length === 1){
            return [new Const(+this.mas[index]), 1]
        }
        else
            throw new ParseError('Unexpected token ' + this.mas[index])
    }
}

const parsePrefix = (string) => {
    return new Parser(string, true).parse(0)[0]
}
const parsePostfix = (string) => {
    return new Parser(string, false).parse(0)[0]
}
const parse = function(string) {
    const mas = string.split(/\s/).filter((str) => str.length > 0);
    const stack = []
    for (const item of mas) {
        if (item in indexOfVars)
            stack.push(new Variable(item))
        else if (item in operations)
            stack.push(new operations[item](...stack.splice(stack.length - count_of_operands[item])))
        else if (!Number.isNaN(+item)){
            stack.push(new Const(+item))
        }
        else
            throw new ParseError('Expected expression or operation or variable or const, but found ' + item)
    }
    return stack[0]
}
