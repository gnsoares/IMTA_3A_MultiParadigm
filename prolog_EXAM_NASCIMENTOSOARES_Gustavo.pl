% Nom: NASCIMENTO SOARES
% Prenom: Gustavo

% Question 1
divisible_par_quatre([]).
divisible_par_quatre([_X1, _X2, _X3, _X4 | R]) :- divisible_par_quatre(R).

% Question 2
% get_col(ListofLists, Column, MnoColumn)
get_col([], [], []).
get_col([[H|T]|RowsR], [H|ColR], [T|Rest]) :- get_col(RowsR, ColR, Rest).

% transpose(M, MT)
transpose([], []).
transpose(L, []) :- member([], L).
transpose(M, [Col|ColsR]) :-
    get_col(M, Col, MRest),
    transpose(MRest, ColsR), !.

% Question 3
% remove(Elem, List, ListWoutElem)
remove(K, L, L) :- not(member(K, L)).
remove(K, L1, L2) :- select(K, L1, L2i), remove(K, L2i, L2), !.
% unique(Unique, List)
unique([], []).
unique([X], [X]).
unique([H|UniqueR], [H|Rest]) :- remove(H, Rest, Inter), unique(UniqueR, Inter), !.
% compte(L,K)
compte(L, K) :- unique(U, L), length(U, K).

% Question 4
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
amazon(L) :- no_attack(L).

% Question 5
merge(L, M) :- append(L, A), unique(U, A), msort(U, M).

% Question 6
sous_somme(_, 0, []).
sous_somme(L1, Sum, L2) :-
    msort(L1, L1Asc),
    reverse(L1Asc, L1Des),
    sous_somme_(L1Des, Sum, L2).

sous_somme_([K|_], K, [K]).
sous_somme_([Max|TL1], Sum, [Max|TL2]) :-
    Sum > 0,
    not_bigger_than_total([Max|TL1], Sum),
    pgcd([Max|TL1], G),
    Sum mod G =:= 0,
    NewSum is Sum - Max,
    sous_somme_(TL1, NewSum, TL2).
sous_somme_([_|TL1], Sum, L2) :-
    Sum > 0,
    sous_somme_(TL1, Sum, L2).

not_bigger_than_total([], N) :- N =< 0.
not_bigger_than_total([H|T], N) :-
    NN is N - H,
    not_bigger_than_total(T, NN).

pgcd(X, X, X).
pgcd(X, Y, D) :-
    X < Y,
    Y1 is Y - X,
    pgcd(X, Y1, D).
pgcd(X, Y, D) :-
    X > Y,
    pgcd(Y, X, D). 

pgcd([X], X).
pgcd([X1, X2|T], Res) :-
    pgcd(X1, X2, ResInt),
    pgcd([ResInt|T], Res).
