% to remember:
%   member(X, List),
%   length(List, Length),
%   edit(predicator),
%   listing(predicator),
%   select(Element, List, ListWithoutElement),
%   ; is or
%   , is and
%   not() is not
%   comparison: =, \=
%   arithmetic comparison: <, >, >=, =<, =:=, =\=
%   arithmetic operations: +, -, +, % (div), // (div int), mod, sqrt, max
%   (if P then Q else R)
%   (P -> Q ; R) :- P, !, Q.
%   (P -> Q ; R) :- R.

% 2.a. puissance(X, N, V) s.t. V = X^N
puissance(X, 0, 1) :- X >= 1.
puissance(X, N, V) :- puissance(X, N1, V1), V is V1*X, N is N1+1, !.

% 2.b. entre(I, J, K) s.t. I < K < J
entreList(I, J, []) :- I >= J.
entreList(I, J, [I|Ks]) :- I < J, Prox is I+1, entreList(Prox, J, Ks).
entre(I, J, K) :- K is I + 1, K is J - 1, !.
entre(I, J, K) :- entreList(I, J, [I|Ks]), member(K, Ks).

% 3.a. evenLength(L) s.t. true if length of L is even
evenLength([]).
evenLength([_]) :- false.
evenLength([_|Xs]) :- not(evenLength(Xs)).

% 3.b. add(L, N) s.t. N = sum(L)
add([], 0).
add([X|Xs], V) :- add(Xs, Vi), V is Vi + X.

% 3.c. double(L1, L2) s.t. each element in L1 appears twice in L2
double([], []).
double([X|Xs], [X,X|Rest]) :- double(Xs, Rest).

% 3.4. last(X, L)
last1(X, [X]).
last1(X, [_|L]) :- last1(X, L).

last2([X|Xs], L) :- last_(Xs, X, L).

last_([], L, L).
last_([X|Xs], _, L) :- last_(Xs, X, L).

% 3.5. sousListe(SL, L)
sousListe([], _).
sousListe([X|SL], [X|L]) :- sousListe(SL, L).
sousListe([X|SL], [_|L]) :- sousListe([X|SL], L).

% 3.6. sousSomme(L1, N, L2)
sousSomme(L1, N, L2) :- sousListe(L2, L1), add(L2, N).

% 3.7. permutation(L, P)
permutation1([], []).
permutation1(List, [First|Perm]) :-
    select(First, List, Rest),
    permutation1(Rest, Perm).

% 4.
arc(a, x, 4).
arc(a, b, 7).
arc(a, c, 5).
arc(b, x, 8).
arc(b, c, 4).
arc(b, d, 3).
arc(b, e, 6).
arc(c, f, 6).
arc(d, e, 7).
arc(e, f, 5).
connect(X,X,[X],0).
connect(X,Y,[X|Xs],N) :- arc(X,Z,N1), connect(Z,Y,Xs,N2), N is N1 + N2.


%%%%%%%%%%%%%
inc_list([], []).
inc_list([H|T], [HR|TR]) :- HR is H+1, inc_list(T, TR).
which_asc_diagonal([], []).
which_asc_diagonal([H|T], [HI|TD]) :-
    inc_list([H|T], [HI|TI]),
    which_asc_diagonal(TI, TD).

dec_list([], []).
dec_list([H|T], [HR|TR]) :- HR is H-1, dec_list(T, TR).
which_des_diagonal([], []).
which_des_diagonal([H|T], [HI|TD]) :-
    dec_list([H|T], [HI|TI]),
    which_des_diagonal(TI, TD).


amazon(L) :-
    unique(L, L),
    max_list(L, N),
    length(L, N),
    min_list(L, 1),
    which_asc_diagonal(L, AscDiags),
    unique(AscDiags, AscDiags),
    which_des_diagonal(L, DesDiags),
    unique(DesDiags, DesDiags).

no_attack([]).
no_attack([X|Y]) :- no_attack(Y,X,1), no_attack(Y).
no_attack([],_,_).
no_attack([Y|Z],X,Dist) :-
    X \== Y,
    X2 is X-Dist, X2 \== Y,
    X1 is X+Dist, X1 \== Y,
    DistProx is Dist+1, no_attack(Z,X,DistProx),
    HorseDiff is Y - X,
    abs(HorseDiff, AbsHorseDiff),
    D is 1 -> AbsHorseDiff \== 2,
    D is 2 -> AbsHorseDiff \== 1.

% range(N, L) :- range_(N, [], L).
% range_(0, [], []).
% range_(N, Acc, []) :- range_(N1, , )
is_solution(X) :-
    permutation([1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16], X),
    no_attack(X).
