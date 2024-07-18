'use strict'
// :NOTE: - Extra '
const types = {'Debug':0, 'Info':1, 'Warning':2, "Error":3}
// :NOTE: * Использование строк вместо констант

function LogError(message) {
    this.message = message;
}
LogError.prototype = Object.create(Error.prototype);
LogError.prototype.name = "LogError";
LogError.prototype.constructor = LogError;

function Log (message, {type="Debug", data=true, time=true}){
    if (!(type in types)){
        throw new LogError("Unknown log type '" + type + "'");
    }
    if (typeof(data) != "boolean"){
        throw new LogError("Expected boolean value for data, but found " + typeof(data));
    }
    if (typeof(time) != "boolean"){
        throw new LogError("Expected boolean value for data, but found " + typeof(time));
    }
    this.toString = function() {
        const date = new Date();
        return `${type}${data ? " " + date.toLocaleDateString(): ''}${time ? " " + date.toTimeString(): ''}${" " + message}`;
    }
    this.getLvl = () => types[type];
}
function Logger(logging, lvl) {
    this.log = function(message, args={}){
        const newLog = new Log(message, args);
        if (lvl <= newLog.getLvl()){
            logging(newLog.toString());
        }
    }
}
function LoggerConstructor(Child, log, lvl="Debug") {
    Logger.call(this, log, types[lvl]);
    Child.prototype = Object.create(Child.prototype);
    Child.prototype.constructor = Child;
}

function ConsoleLogger(...args) {
    LoggerConstructor.call(this, ConsoleLogger, console.log, ...args);
}

function HTMLLogger(...args) {
    LoggerConstructor.call(this, HTMLLogger, function(message){
        document.getElementById("out").innerHTML += message + "\n"}, ...args);
}
function CompositeLogger(...args) {
    // :NOTE: * Не расширяемо
  for (const item in args){
    if (args[item].constructor.name == 'ConsoleLogger'){
      break
    }
    if (args[item].constructor.name == 'HTMLLogger'){
      break
    }
    if (args[item].constructor.name == 'CompositeLogger'){
      break
    }
    throw new LogError('Expected all arguments - loggers')
  }
  this.log = (message, ...arg) => args.map((x) => x.log(message, ...arg))
}
var con = new ConsoleLogger("Warning")
var html = new HTMLLogger()
var comp = new CompositeLogger(con, html)
comp.log('debug, data and time')
comp.log("error only data", {type:"Error", time:false})
con.log("only log")
html.log('unknown arg', {'sth':123})
// html.log("wrong type", {type:"smth"}) - ERROR
//var wrong = new CompositeLogger("string") - ERROR
//var wrong = new CompositeLogger(con, "string") - ERROR