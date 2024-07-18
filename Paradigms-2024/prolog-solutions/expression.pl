% Из lists.pl
lookup(K, [(K, V) | _], V).
lookup(K, [_ | T], V) :- lookup(K, T, V).


% Конструкторы
variable(Name, variable(Name)).
const(Value, const(Value)).

op_add(A, B, operation(op_add, A, B)).
op_subtract(A, B, operation(op_subtract, A, B)).
op_multiply(A, B, operation(op_multiply, A, B)).
op_divide(A, B, operation(op_divide, A, B)).
op_negate(A, operation(op_negate, A)).
op_bitnot (A, operation(op_bitnot, A)).
op_bitand (A, B, operation(op_bitand, A, B)).
op_bitor (A, B, operation(op_bitor, A, B)).
op_bitxor (A, B, operation(op_bitxor, A, B)).
op_bitif (A, B, C, operation(op_bitif, A, B, C)).
op_bitmux(A, B, C, operation(op_bitmux, A, B, C)).



% Определение операций
operation(op_add, A, B, R) :- R is A + B.
operation(op_subtract, A, B, R) :- R is A - B.
operation(op_multiply, A, B, R) :- R is A * B.
operation(op_divide, A, 0, R) :- !, R is A / 0.0.
operation(op_divide, A, B, R) :- !, R is A / B.
operation(op_bitand, A, B, R) :- R is A /\ B.
operation(op_bitor, A, B, R) :- R is A \/ B.
operation(op_bitxor, A, B, R) :- R is (A \/ B) /\ (\(A/\B)).
operation(op_negate, A, R) :- R is -A.
operation(op_bitnot, A, R) :- R is \A.
operation(op_bitif, A, B, C, R):- 1 is mod(A, 2), !, R is B.
operation(op_bitif, A, B, C, R):- 0 is mod(A, 2), !, R is C.
operation(op_bitmux, A, B, C, R):- operation(op_bitnot, A, NA), operation(op_bitand, A, C, And1), operation(op_bitand, NA, B, And2), operation(op_bitor, And1, And2, R).

down("X", 'x'):-!.
down("Y", 'y'):-!.
down("Z", 'z'):-!.
down(S, S).

% Вычисление выражений
evaluate(const(Value), _, Value).
evaluate(variable(Name), Vars, R) :- atom_chars(Name, [First|_]), down(First, Lower), lookup(Lower, Vars, R).
evaluate(operation(Op, A), Vars, R) :-
    evaluate(A, Vars, AV),
    operation(Op, AV, R).
evaluate(operation(Op, A, B), Vars, R) :-
    evaluate(A, Vars, AV),
    evaluate(B, Vars, BV),
    operation(Op, AV, BV, R).
evaluate(operation(Op, A, B, C), Vars, R) :-
    evaluate(A, Vars, AV),
    evaluate(B, Vars, BV),
    evaluate(C, Vars, CV),
    operation(Op, AV, BV, CV, R).

% Из terms.pl
nonvar(V, _) :- var(V).
nonvar(V, T) :- nonvar(V), call(T).


% Загружаем библиотеку (DCG = Definite Clause Grammar)
:- load_library('alice.tuprolog.lib.DCGLibrary').

% Грамматика для переменных
infix_p(variable(Name)) --> 
  {nonvar(Name, atom_chars(Name, Chars))},
  letters_p(Chars),
  {Chars = [_ | _], atom_chars(Name, Chars)}.
  
letters_p([]) --> [].
letters_p([H | T]) -->
  { member(H, [x, y, z, 'X', 'Y', 'Z'])},
  [H],
  letters_p(T).

% Грамматика для констант
infix_p(const(Value)) -->
  {nonvar(Value, number_chars(Value, Chars))},
  digits_n(Chars),
  {Chars = [_ | _], \+ Chars = ['-'], number_chars(Value, Chars)}.

digits_n(['-' | T]) --> ['-'], digits_p(T).
digits_n(M) --> digits_p(M).
digits_p([]) --> [].
digits_p([H | T]) -->
  { member(H, ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'])},
  [H],
  digits_p(T).

% Грамматика для символов операций

op_p(op_negate) --> ['n', 'e', 'g', 'a', 't', 'e'].
op_p(op_add) --> ['+'].
op_p(op_subtract) --> ['-'].
op_p(op_multiply) --> ['*'].
op_p(op_divide) --> ['/'].
op_p(op_bitnot) --> ['~'].
op_p(op_bitand) --> ['&'].
op_p(op_bitor) --> ['|'].
op_p(op_bitxor) --> ['^'].
op_p(op_bitif) --> ['?'].
op_p(op_bitmux) --> ['¿'].


% Грамматика для операций
infix_p(operation(Op, A, B, C)) --> {nonvar(Op)}, ['('], infix_p(A),[' '], op_p(Op),[' '], infix_p(B), [' : '], infix_p(C), [')'].
infix_p(operation(Op, A, B, C)) --> {var(Op)}, ['('], infix_p(A), op_p(Op), infix_p(B),[':'], infix_p(C), [')'].
infix_p(operation(Op, A, B)) --> {nonvar(Op)}, ['('], infix_p(A),[' '], op_p(Op),[' '], infix_p(B), [')'].
infix_p(operation(Op, A, B)) --> {var(Op)}, ['('], infix_p(A), op_p(Op), infix_p(B), [')'].
infix_p(operation(Op, A)) --> {var(Op)}, op_p(Op), infix_p(A).
infix_p(operation(Op, A)) --> {nonvar(Op)}, op_p(Op), [' '], infix_p(A).

filter_spaces([], []).
filter_spaces([X|Xs], Ys) :- 
    member(X, [' ', '\n', '\t']),
    !,
    filter_spaces(Xs, Ys).
filter_spaces([X|Xs], [X|Ys]) :- 
    filter_spaces(Xs, Ys).

% Преобразование в строку
infix_str(E, A) :- ground(E), once(phrase(infix_p(E), C)), atom_chars(A, C).
infix_str(E, A) :-   atom(A), atom_chars(A, C), filter_spaces(C, NC), once(phrase(infix_p(E), NC)).
