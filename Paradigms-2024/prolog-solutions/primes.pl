get_comp(N, I, J):- N >= J, assert(composite(J)), NJ is J + I * 2, get_comp(N, I, NJ).

iteration(N, I):- \+ composite(I), assert(primes(I)), I2 is I * I, get_comp(N, I, I2).

erat(N, I) :- iteration(N, I); N >= I * I, I1 is I + 1, erat(N, I1).

binoptimize(N, I, J):- N >= J, assert(composite(J)), NJ is J + I, binoptimize(N, I, NJ).

init(MAX_N):- assert(primes(2)), (binoptimize(MAX_N, 2, 4); erat(MAX_N, 3); true), primeslist(L), assert(primestolist(L)).

prime(N) :- \+ composite(N).

fact(N, D, ANS, NN):- 0 is mod(N, D), !, N1 is N / D, fact(N1, D, ANS1, T), NN = T, append(ANS1, [D], ANS).
fact(N, _, [], N).

to_primes(1, _, [], _) :-!.
to_primes(N, S, ANS, []) :- !, ANS = [N].
to_primes(N, S, ANS, [H|T]) :- 0 is mod(N, H), !, fact(N, H, ANS1, NN),  to_primes(NN, S, ANS2, T), append(ANS1, ANS2, ANS).
to_primes(N, S, ANS, [H|T]) :- to_primes(N, S, ANS, T).

smart_multiply(1, _, []) :-!.
smart_multiply(N, L, [H | T]) :- H = L, !, smart_multiply(N1, H, T), N is H * N1.
smart_multiply(N, L, [H | T]) :- H > L, !, prime(H), smart_multiply(N1, H, T), N is H * N1.

prime_divisors(N, Divisors) :- list(Divisors), !, smart_multiply(R, 1, Divisors), N = R.
prime_divisors(N, Divisors) :- number(N), !, primestolist(L), to_primes(N, N, Divisors, L).

primeslist(L) :- findall((X), primes(X), L).

intersect([], _, 1):- !.
intersect(_, [], 1):- !.
intersect([H1|T1], [H2|T2], ANS) :- H1=H2, !,  intersect(T1, T2, NANS), ANS is NANS * H1.
intersect([H1|T1], [H2|T2], ANS) :- H1 < H2, !, append([H2], T2, L2), intersect(T1, L2, NANS), ANS = NANS.
intersect([H1|T1], [H2|T2], ANS) :- H1 > H2, !, append([H1], T1, L1), intersect(L1, T2, NANS), ANS = NANS.

union([], [], 1):- !.
union([H1|T1], [], ANS):- !, union(T1, [], NANS), ANS is NANS * H1.
union([], [H2|T2], ANS):- !, union([], T2, NANS), ANS is NANS * H2.
union([H1|T1], [H2|T2], ANS) :- H1=H2, !,  union(T1, T2, NANS), ANS is NANS * H1.
union([H1|T1], [H2|T2], ANS) :- H1 < H2, !, append([H2], T2, L2), union(T1, L2, NANS), ANS is NANS * H1.
union([H1|T1], [H2|T2], ANS) :- H1 > H2, !, append([H1], T1, L1), union(L1, T2, NANS), ANS is NANS * H2.


gcd(A, B, GCD):- prime_divisors(A, FA), prime_divisors(B, FB), intersect(FA, FB, GCD).
lcm(A, B, LCM):- prime_divisors(A, FA), prime_divisors(B, FB), union(FA, FB, LCM).
