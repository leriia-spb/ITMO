(defn linearVector? [v] (and (vector? v) (every? number? v)))
(defn sameSizeUnit? [& units] (apply = (map #(count %) units)))
(defn sameSizeMatrix? [& ms]
  (and (apply sameSizeUnit? ms)
       (apply sameSizeUnit?
         (map #(nth % 0) ms))))
(defn matrix? [m] (and (vector? m) (every? linearVector? m) (apply sameSizeUnit? m)))
(defn generalFunction [checkType checkCol func]
  (fn [& units]
    {:pre [(every? checkType units) (apply checkCol units)]}
    (apply mapv func units)))
(def v+ (generalFunction linearVector? sameSizeUnit? +'))
(def v- (generalFunction linearVector? sameSizeUnit? -'))
(def v* (generalFunction linearVector? sameSizeUnit? *'))
(def vd (generalFunction linearVector? sameSizeUnit? /))
(defn scalar [& vs]
  {:pre [(every? linearVector? vs) (apply sameSizeUnit? vs)]}
  (reduce + 0 (apply v* vs)) )
(defn vect [& vs]
  {:pre [(every? linearVector? vs) (apply sameSizeUnit? vs)]}
  (reduce (fn [[x1 y1 z1] [ x2 y2 z2]]
            (vector (- (* y1 z2) (* y2 z1)) (- (* x2 z1) (* x1 z2)) (- (* x1 y2) (* x2 y1)))) vs))
(defn v*s [v & s]
  {:pre [(linearVector? v) (every? number? s)]}
  (mapv #(* % (apply * s)) v))
(def m+ (generalFunction matrix? sameSizeMatrix? v+))
(def m- (generalFunction matrix? sameSizeMatrix? v-))
(def m* (generalFunction matrix? sameSizeMatrix? v*))
(def md (generalFunction matrix? sameSizeMatrix? vd))
(defn m*s [m & s]
  {:pre [(matrix? m) (every? number? s)]}
  (mapv #(v*s % (apply * s)) m))
(defn m*v [m v]
  {:pre [(matrix? m) (linearVector? v)]}
  (mapv #(apply + (v* % v)) m))
(defn transpose [m]
  {:pre [(matrix? m)]}
  (apply mapv vector m))
(defn m*m [& ms]
  {:pre [(every? matrix? ms)]}
  (reduce
    (fn [m1 m2]
      (transpose (mapv #(m*v m1 %) (transpose m2))) ) ms))
(defn form [t]
  {:pre [(or (number? t) (not (empty? t))) ]
   }
  (cond
    (number? t) (list)
    :else (conj (form (nth t 0)) (count t))))
(defn tensor? [t form]
  (cond
    (empty? form) (number? t)
    :else (and (vector? t) (= (count t) (first form)) (mapv #(tensor? % (rest form)) t))))

(defn broadcast? [needForm form]
  (cond
    (and (empty? needForm) (empty? form)) true
    ( > (count form) (count needForm)) false
    ( < (count form) (count needForm)) (broadcast? (rest needForm) form)
    :else (and (= (first form) (first needForm)) (broadcast? (rest needForm) (rest form)))
    ))
(defn broadcast [t1 t2]
  {:pre [(or (broadcast? (form t1) (form t2)) (broadcast? (form t2) (form t1)))]}
  (letfn [(broadcasting [t needForm form]
            (cond
              (= (count needForm) (count form)) t
              :else (mapv (constantly (broadcasting t (rest needForm) form)) (range (first needForm)))
              ))]
    (cond
      (broadcast? (form t1) (form t2)) (list t1 (broadcasting t2 (form t1) (form t2)))
      :else (list (broadcasting t1 (form t2) (form t1)) t2))))
(defn generalForTensors [func]
  (fn [& ts]
    {:pre [(map #(tensor? % (form %)) ts)] }
    (letfn [(doOperation [& ts]
              (cond
                (every? number? ts) (apply func ts)
                :else (mapv #(apply doOperation %)
                            (map (apply mapv vector ts)
                                 (range (count (nth ts 0)))))))]
      (cond
         (= (count ts) 1) (apply doOperation ts)
         :else (reduce #(apply doOperation (broadcast %1 %2)) ts)))))
(def b+ (generalForTensors +'))
(def b- (generalForTensors -'))
(def b* (generalForTensors *'))
(def bd (generalForTensors /))
