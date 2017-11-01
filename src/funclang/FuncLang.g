grammar FuncLang;
 
import ListLang; //Import all rules from ListLang grammar.
 
 // New elements in the Grammar of this Programming Language
 //  - grammar rules start with lowercase

 exp returns [Exp ast]: 
		va=varexp { $ast = $va.ast; }
		| num=numexp { $ast = $num.ast; }
		| str=strexp { $ast = $str.ast; }
		| bl=boolexp { $ast = $bl.ast; }
        | add=addexp { $ast = $add.ast; }
        | sub=subexp { $ast = $sub.ast; }
        | mul=multexp { $ast = $mul.ast; }
        | div=divexp { $ast = $div.ast; }
        | let=letexp { $ast = $let.ast; }
        | lam=lambdaexp { $ast = $lam.ast; }
        | call=callexp { $ast = $call.ast; }
        | i=ifexp { $ast = $i.ast; }
        | less=lessexp { $ast = $less.ast; }
        | eq=equalexp { $ast = $eq.ast; }
        | gt=greaterexp { $ast = $gt.ast; }
        | car=carexp { $ast = $car.ast; }
        | cdr=cdrexp { $ast = $cdr.ast; }
        | cons=consexp { $ast = $cons.ast; }
        | list=listexp { $ast = $list.ast; }
        | nl=nullexp { $ast = $nl.ast; }
        | npe=numberpred { $ast = $npe.ast; }
        | bpe=booleanpred { $ast = $bpe.ast; }
        | spe=stringpred { $ast = $spe.ast; }
        | ppe=procedurepred { $ast = $ppe.ast; }
        | rpe=pairpred { $ast = $rpe.ast; }
        | lpe=listpred { $ast = $lpe.ast; }
        | upe=unitpred { $ast = $upe.ast; }
        | re=refexp { $ast = $re.ast; }
        | dre=derefexp { $ast = $dre.ast; }
        | ass=assignexp { $ast = $ass.ast; }
        | free=freeexp { $ast = $free.ast; }
        | loc=locexp { $ast = $loc.ast; }
        ;

 locexp returns [LocExp ast] :
        '(' 'loc'
            e=exp
        ')' { $ast = new LocExp($e.ast); }
        ;

 refexp returns [RefExp ast] :
        '(' 'ref'
            e=exp
        ')' { $ast = new RefExp($e.ast); }
        ;

 derefexp returns [DerefExp ast] :
        '(' 'deref'
            e=locexp
        ')' { $ast = new DerefExp($e.ast); }
        ;

 assignexp returns [AssignExp ast] :
        '(' 'assign'
            e1=locexp
            e2=exp
        ')' { $ast = new AssignExp($e1.ast, $e2.ast); }
        ;

 freeexp returns [FreeExp ast] :
        '(' 'free'
            e=locexp
        ')' { $ast = new FreeExp($e.ast); }
        ;

 lambdaexp returns [LambdaExp ast]
        locals [ArrayList<String> formals, String optlastid, Exp optlastexp ]
 		@init { $formals = new ArrayList<String>(); $optlastid = null; } :
 		'(' Lambda 
 			'('
 			    (id=Identifier { $formals.add($id.text); } )*
 			    ('(' lastid=Identifier '=' lastexp=exp ')' { $optlastid = $lastid.text; $optlastexp = $lastexp.ast; } )?
 			')'
 			body=exp 
 		')' { $ast = ($optlastid == null) ? new LambdaExp($formals, $body.ast) : new LambdaExp($formals, $body.ast, $optlastid, $optlastexp); }
 		;

 callexp returns [CallExp ast] 
        locals [ArrayList<Exp> arguments = new ArrayList<Exp>();  ] :
 		'(' f=exp 
 			( e=exp { $arguments.add($e.ast); } )* 
 		')' { $ast = new CallExp($f.ast,$arguments); }
 		;

 ifexp returns [IfExp ast] :
 		'(' If 
 		    e1=exp 
 			e2=exp 
 			e3=exp 
 		')' { $ast = new IfExp($e1.ast,$e2.ast,$e3.ast); }
 		;

 lessexp returns [LessExp ast] :
 		'(' Less 
 		    e1=exp 
 			e2=exp 
 		')' { $ast = new LessExp($e1.ast,$e2.ast); }
 		;

 equalexp returns [EqualExp ast] :
 		'(' Equal 
 		    e1=exp 
 			e2=exp 
 		')' { $ast = new EqualExp($e1.ast,$e2.ast); }
 		;

 greaterexp returns [GreaterExp ast] :
 		'(' Greater
 		    e1=exp
 			e2=exp
 		')' { $ast = new GreaterExp($e1.ast,$e2.ast); }
 		;

numberpred returns [NumberPredExp ast] :
 		'(' 'number?'
 		    e=exp
 		')' { $ast = new NumberPredExp($e.ast); }
 		;

booleanpred returns [BooleanPredExp ast] :
 		'(' 'boolean?'
 		    e=exp
 		')' { $ast = new BooleanPredExp($e.ast); }
 		;

stringpred returns [StringPredExp ast] :
 		'(' 'string?'
 		    e=exp
 		')' { $ast = new StringPredExp($e.ast); }
 		;

procedurepred returns [ProcedurePredExp ast] :
 		'(' 'procedure?'
 		    e=exp
 		')' { $ast = new ProcedurePredExp($e.ast); }
 		;

pairpred returns [PairPredExp ast] :
 		'(' 'pair?'
 		    e=exp
 		')' { $ast = new PairPredExp($e.ast); }
 		;

listpred returns [ListPredExp ast] :
 		'(' 'list?'
 		    e=exp
 		')' { $ast = new ListPredExp($e.ast); }
 		;

unitpred returns [UnitPredExp ast] :
 		'(' 'unit?'
 		    e=exp
 		')' { $ast = new UnitPredExp($e.ast); }
 		;
