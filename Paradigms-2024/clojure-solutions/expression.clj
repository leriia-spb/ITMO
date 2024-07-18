(load-file "proto.clj")
(load-file "parser.clj")
;(load-file "paradigms/clojure-solutions/proto.clj")
;(load-file "paradigms/clojure-solutions/parser.clj")

(defn div
  ([x] (div 1 x))
  ([x & more] (/ x (double (apply *' more)))))
(defn arith [& args] (div (apply +' args) (count args)))
(defn geom [& args] (Math/pow (abs (apply *' args)) (div (count args))))
(defn harm [& args] (div (count args) (apply +' (map #(div %) args))))
;_______________________________Function_________________________________________
(defn constant [c] (constantly c))
(defn variable [name] (fn [vars] (vars name)))
(defn operation [func] (fn [& args] (fn [vars] (apply func (map #(% vars) args)))))
(def add (operation +'))
(def subtract (operation -'))
(def multiply (operation *'))
(def divide (operation div))
(defn negate [arg] (fn [vars] (- (arg vars))))
(def arithMean (operation arith))
(def geomMean (operation geom))
(def harmMean (operation harm))
;_______________________________Object_________________________________________
(def toString (method :toString))
(def evaluate (method :evaluate))
(def diff (method :diff))
(def toStringInfix (method :toStringInfix))
(defn PrimitiveType [diff eval] (constructor (fn [this num] (assoc this
                                                              :str num
                                                              :toString (fn [this] (str (this :str)))
                                                              :toStringInfix (fn [this] (str (this :str)))))
                                             {:diff     diff
                                              :evaluate eval}))

(declare Zero)
(def Constant (PrimitiveType (fn [_ _] Zero) (fn [this _] (this :str))))
(def Zero (Constant 0))
(def One (Constant 1))
(def Two (Constant 2))
(def Variable (PrimitiveType
                (fn [this arg] (cond (= arg (this :str)) One :else Zero))
                (fn [this vars] (vars (clojure.string/lower-case (first (this :str)))))))
(defn Operation [name operation dif]
  (constructor (fn [this & args]
                 (assoc this
                   :args (apply vector args)
                   :evaluate (fn [this vars] (apply operation (map #(evaluate % vars) (this :args))))
                   :toString (fn [this] (str "(" name (clojure.string/join #" " (map toString (this :args))) ")"))
                   :toStringInfix (fn [this] (str "(" (clojure.string/join (str " " name) (map toStringInfix (this :args))) ")"))))
               {
                :diff (fn [this arg] (apply dif arg (this :args) (map #(diff % arg) (this :args))))
                }))
(defn UnaryOperation [before after beforeI afterI operation dif]
  (constructor (fn [this arg]
                 (assoc this
                   :arg arg
                   :evaluate (fn [this vars] (operation (evaluate (this :arg) vars)))
                   :toString (fn [this] (str before (toString (this :arg)) after))
                   :toStringInfix (fn [this] (str beforeI (toStringInfix (this :arg)) afterI))))
               {
                :diff (fn [this arg] (dif arg (this :arg) (diff (this :arg) arg)))
                }))
(def Add (Operation "+ " +' (fn [_ _ & diffArgs] (apply Add diffArgs))))
(def SinC (Operation "sinc "
                     (fn [y x]
                       (* (Math/sin x) (Math/cosh y)))
                     (fn [_ _ & _] Zero)))
(def CosC (Operation "cosc "
                     (fn [y x]
                       (* (Math/cos x) (Math/cosh y)))
                     (fn [_ _ & _] Zero)))
(def Subtract (Operation "- " -' (fn [_ _ & diffArgs] (apply Subtract diffArgs))))
(def Multiply (Operation "* " *'
                         (fn [_ args & diffArgs]
                           (apply Add
                                  (map #(apply Multiply %)
                                       (map #(update args % (fn [obj] (nth diffArgs (.indexOf args obj))))
                                            (range (count args))))))))
(def Divide (Operation "/ " div (fn [arg args & diffArgs]
                                  (let [denom (apply Multiply (rest args))]
                                    (letfn [(diff2 [arg1 arg2 dArg1 dArg2]
                                              (Divide
                                                (Subtract
                                                  (Multiply dArg1 arg2)
                                                  (Multiply arg1 dArg2))
                                                (Multiply arg2 arg2)))]
                                      (cond
                                        (= 1 (count args)) (diff2 One (nth args 0) Zero (nth diffArgs 0))
                                        :else (diff2 (nth args 0) denom (nth diffArgs 0) (diff denom arg))))))))
(def Negate (UnaryOperation "(negate " ")" "negate " "" - (fn [_, _, dif] (Negate dif))))
(def Sin (UnaryOperation "sin " "" "sin " ""
                         #(Math/sin %)
                         (fn [_ _] Zero)))
(def Cos (UnaryOperation "cos " "" "cos " ""
                         #(Math/cos %)
                         (fn [_ _] Zero)))
(def SinP (UnaryOperation "(" " sinp)" "(" " sinp)" (constantly 0) (fn [_ _] Zero)))
(def CosP (UnaryOperation "(" " cosp)" "(" " cosp)" #(Math/cosh %) (fn [_ _] Zero)))
(def ArithMean
  (Operation "arithMean " arith
             (fn [_ args & diffArgs] (Divide (apply Add diffArgs) (Constant (count args))))))

(def GeomMean (Operation "geomMean " geom
                         (fn [arg args & _]
                           (let [thisGeom (apply GeomMean args)]
                             (Divide
                               (diff (apply Multiply args) arg)
                               (Constant (count args))
                               (reduce Multiply (map (constantly thisGeom) (range (- (count args) 1)))))
                             ))))

(def HarmMean (Operation "harmMean " harm
                         (fn [_ args & diffArgs]
                           (let [sumDivs (apply Add (map #(Divide %) args))]
                             (Multiply (Constant (count args))
                                       (Divide
                                         (apply Add (map #(Divide (nth diffArgs (.indexOf args %))
                                                                  (Multiply % %)) args))
                                         (Multiply sumDivs sumDivs)))))))


;_____________________________Parser_______________________________________

(def funcs {'+        add '- subtract '* multiply '/ divide 'negate negate 'arithMean arithMean 'geomMean geomMean
            'harmMean harmMean})
(def objects {'+        Add '- Subtract '* Multiply '/ Divide 'negate Negate 'arithMean ArithMean 'geomMean GeomMean
              'harmMean HarmMean 'sin Sin 'cos Cos 'sinp SinP 'cosp CosP 'sinc SinC 'cosc CosC})
(defn parser [arg vars con mas]
  (cond
    (number? arg) (con arg)
    (symbol? arg) (vars (name arg))
    :else (apply (mas (first arg)) (map #(parser % vars con mas) (rest arg)))))
(defn parseFunction [arg] (parser (read-string arg) variable constant funcs))
(defn parseObject [arg] (parser (read-string arg) Variable Constant objects))
;_____________________________parser_______________________________________
(def parseObjectInfix
  (let
    [allOperations [["sinp" "cosp"]
                    ["negate" "sin" "cos"]
                    ["sinc" "cosc"]
                    ["*" "/"]
                    ["+" "-"]]
     *variable (+seqf #(Variable %) (+str (+plus (+char "xyzXYZ"))))
     *int (+seqf #(Constant (Integer/parseInt %)) (+seqf str
                                                         (+str (+star (+char "+-")))
                                                         (+str (+plus (+char "0123456789")))))
     *float (+seqf #(Constant (Float/parseFloat %))
                   (+seqf str
                          (+str (+star (+char "+-")))
                          (+str (+plus (+char "0123456789")))
                          (+char ".")
                          (+str (+plus (+char "0123456789")))))
     *const (+or *float *int)
     *space (+char " \t\n\r")
     *ws (+ignore (+star *space))]
    (letfn [(*priorityOperator [num]
              (apply +or
                     (map
                       (fn [name] (+str (apply +seq (map #(+char (str %)) name))))
                       (nth allOperations num))))
            (*seq [begin parser end]
              (+seqn 1 (+char begin) *ws parser *ws (+char end)))
            (*brackets [] (*seq "(" (delay (*operation 4)) ")"))
            (*binRightOperation []
              (+seqf (fn [funcs arg] (reduce #(%2 %) arg (reverse funcs)))
                     (+star (+seqf
                              #(fn [l] ((objects (symbol (str %2))) %1 l))
                              *ws
                              (*unaryPostOperation)
                              *ws
                              (*priorityOperator 2)
                              *ws))
                     (*unaryPostOperation)))
            (*binLeftOperation [num]
              (+seqf
                (fn [arg funcs] (reduce #(%2 %) arg funcs))
                (*operation (- num 1)) (+star (+seqf
                                                #(fn [l] ((objects (symbol (str %1))) l %2))
                                                *ws
                                                (*priorityOperator num)
                                                *ws
                                                (*operation (- num 1))
                                                *ws))))
            (*unaryPostOperation []
              (+seqf (fn [arg funcs] (reduce #((objects (symbol (str %2))) %1) arg funcs))
                     *ws (*unaryPrefOperation) *ws
                     (+star (+seqn 0 *ws
                                   (*priorityOperator 0) *ws))))
            (*unaryPrefOperation []
              (+seqf (fn [funcs arg] (reduce #((objects (symbol (str %2))) %1) arg (reverse funcs)))
                     (+star (+seqn 0 *ws
                                   (*priorityOperator 1) *ws))
                     *ws (+or *variable *const (*brackets) ) *ws))
            (*operation [num] (cond
                                (= num 2) (*binRightOperation)
                                :else (*binLeftOperation num)))]
      (+parser (+seqn 0 *ws (*operation 4) *ws)))))