"use strict";(self.webpackChunkdocsite=self.webpackChunkdocsite||[]).push([[3888],{3888:(t,e,n)=>{n.d(e,{d:()=>B,p:()=>c,s:()=>b});var i=n(4646),u=n(1896),s=n(3622),r=n(4724),a=n(8553),o=function(){var t=function(t,e,n,i){for(n=n||{},i=t.length;i--;n[t[i]]=e);return n},e=[1,3],n=[1,7],i=[1,8],u=[1,9],s=[1,10],r=[1,13],a=[1,12],o=[1,16,25],c=[1,20],l=[1,32],h=[1,33],A=[1,34],p=[1,36],d=[1,39],y=[1,37],E=[1,38],C=[1,44],g=[1,45],F=[1,40],m=[1,41],k=[1,42],f=[1,43],D=[1,48],_=[1,49],B=[1,50],b=[1,51],T=[16,25],S=[1,65],v=[1,66],N=[1,67],L=[1,68],$=[1,69],O=[1,70],I=[1,71],x=[1,80],R=[16,25,32,45,46,54,60,61,62,63,64,65,66,71,73],P=[16,25,30,32,45,46,50,54,60,61,62,63,64,65,66,71,73,88,89,90,91],w=[5,8,9,10,11,16,19,23,25],G=[54,88,89,90,91],U=[54,65,66,88,89,90,91],M=[54,60,61,62,63,64,88,89,90,91],Y=[16,25,32],z=[1,107],K={trace:function(){},yy:{},symbols_:{error:2,start:3,mermaidDoc:4,statments:5,direction:6,directive:7,direction_tb:8,direction_bt:9,direction_rl:10,direction_lr:11,graphConfig:12,openDirective:13,typeDirective:14,closeDirective:15,NEWLINE:16,":":17,argDirective:18,open_directive:19,type_directive:20,arg_directive:21,close_directive:22,CLASS_DIAGRAM:23,statements:24,EOF:25,statement:26,className:27,alphaNumToken:28,classLiteralName:29,GENERICTYPE:30,relationStatement:31,LABEL:32,classStatement:33,methodStatement:34,annotationStatement:35,clickStatement:36,cssClassStatement:37,noteStatement:38,acc_title:39,acc_title_value:40,acc_descr:41,acc_descr_value:42,acc_descr_multiline_value:43,CLASS:44,STYLE_SEPARATOR:45,STRUCT_START:46,members:47,STRUCT_STOP:48,ANNOTATION_START:49,ANNOTATION_END:50,MEMBER:51,SEPARATOR:52,relation:53,STR:54,NOTE_FOR:55,noteText:56,NOTE:57,relationType:58,lineType:59,AGGREGATION:60,EXTENSION:61,COMPOSITION:62,DEPENDENCY:63,LOLLIPOP:64,LINE:65,DOTTED_LINE:66,CALLBACK:67,LINK:68,LINK_TARGET:69,CLICK:70,CALLBACK_NAME:71,CALLBACK_ARGS:72,HREF:73,CSSCLASS:74,commentToken:75,textToken:76,graphCodeTokens:77,textNoTagsToken:78,TAGSTART:79,TAGEND:80,"==":81,"--":82,PCT:83,DEFAULT:84,SPACE:85,MINUS:86,keywords:87,UNICODE_TEXT:88,NUM:89,ALPHA:90,BQUOTE_STR:91,$accept:0,$end:1},terminals_:{2:"error",5:"statments",8:"direction_tb",9:"direction_bt",10:"direction_rl",11:"direction_lr",16:"NEWLINE",17:":",19:"open_directive",20:"type_directive",21:"arg_directive",22:"close_directive",23:"CLASS_DIAGRAM",25:"EOF",30:"GENERICTYPE",32:"LABEL",39:"acc_title",40:"acc_title_value",41:"acc_descr",42:"acc_descr_value",43:"acc_descr_multiline_value",44:"CLASS",45:"STYLE_SEPARATOR",46:"STRUCT_START",48:"STRUCT_STOP",49:"ANNOTATION_START",50:"ANNOTATION_END",51:"MEMBER",52:"SEPARATOR",54:"STR",55:"NOTE_FOR",57:"NOTE",60:"AGGREGATION",61:"EXTENSION",62:"COMPOSITION",63:"DEPENDENCY",64:"LOLLIPOP",65:"LINE",66:"DOTTED_LINE",67:"CALLBACK",68:"LINK",69:"LINK_TARGET",70:"CLICK",71:"CALLBACK_NAME",72:"CALLBACK_ARGS",73:"HREF",74:"CSSCLASS",77:"graphCodeTokens",79:"TAGSTART",80:"TAGEND",81:"==",82:"--",83:"PCT",84:"DEFAULT",85:"SPACE",86:"MINUS",87:"keywords",88:"UNICODE_TEXT",89:"NUM",90:"ALPHA",91:"BQUOTE_STR"},productions_:[0,[3,1],[3,1],[3,1],[3,2],[6,1],[6,1],[6,1],[6,1],[4,1],[7,4],[7,6],[13,1],[14,1],[18,1],[15,1],[12,4],[24,1],[24,2],[24,3],[27,1],[27,1],[27,2],[27,2],[27,2],[26,1],[26,2],[26,1],[26,1],[26,1],[26,1],[26,1],[26,1],[26,1],[26,1],[26,2],[26,2],[26,1],[33,2],[33,4],[33,5],[33,7],[35,4],[47,1],[47,2],[34,1],[34,2],[34,1],[34,1],[31,3],[31,4],[31,4],[31,5],[38,3],[38,2],[53,3],[53,2],[53,2],[53,1],[58,1],[58,1],[58,1],[58,1],[58,1],[59,1],[59,1],[36,3],[36,4],[36,3],[36,4],[36,4],[36,5],[36,3],[36,4],[36,4],[36,5],[36,3],[36,4],[36,4],[36,5],[37,3],[75,1],[75,1],[76,1],[76,1],[76,1],[76,1],[76,1],[76,1],[76,1],[78,1],[78,1],[78,1],[78,1],[28,1],[28,1],[28,1],[29,1],[56,1]],performAction:function(t,e,n,i,u,s,r){var a=s.length-1;switch(u){case 5:i.setDirection("TB");break;case 6:i.setDirection("BT");break;case 7:i.setDirection("RL");break;case 8:i.setDirection("LR");break;case 12:i.parseDirective("%%{","open_directive");break;case 13:i.parseDirective(s[a],"type_directive");break;case 14:s[a]=s[a].trim().replace(/'/g,'"'),i.parseDirective(s[a],"arg_directive");break;case 15:i.parseDirective("}%%","close_directive","class");break;case 20:case 21:this.$=s[a];break;case 22:this.$=s[a-1]+s[a];break;case 23:case 24:this.$=s[a-1]+"~"+s[a];break;case 25:i.addRelation(s[a]);break;case 26:s[a-1].title=i.cleanupLabel(s[a]),i.addRelation(s[a-1]);break;case 35:this.$=s[a].trim(),i.setAccTitle(this.$);break;case 36:case 37:this.$=s[a].trim(),i.setAccDescription(this.$);break;case 38:i.addClass(s[a]);break;case 39:i.addClass(s[a-2]),i.setCssClass(s[a-2],s[a]);break;case 40:i.addClass(s[a-3]),i.addMembers(s[a-3],s[a-1]);break;case 41:i.addClass(s[a-5]),i.setCssClass(s[a-5],s[a-3]),i.addMembers(s[a-5],s[a-1]);break;case 42:i.addAnnotation(s[a],s[a-2]);break;case 43:this.$=[s[a]];break;case 44:s[a].push(s[a-1]),this.$=s[a];break;case 45:case 47:case 48:break;case 46:i.addMember(s[a-1],i.cleanupLabel(s[a]));break;case 49:this.$={id1:s[a-2],id2:s[a],relation:s[a-1],relationTitle1:"none",relationTitle2:"none"};break;case 50:this.$={id1:s[a-3],id2:s[a],relation:s[a-1],relationTitle1:s[a-2],relationTitle2:"none"};break;case 51:this.$={id1:s[a-3],id2:s[a],relation:s[a-2],relationTitle1:"none",relationTitle2:s[a-1]};break;case 52:this.$={id1:s[a-4],id2:s[a],relation:s[a-2],relationTitle1:s[a-3],relationTitle2:s[a-1]};break;case 53:i.addNote(s[a],s[a-1]);break;case 54:i.addNote(s[a]);break;case 55:this.$={type1:s[a-2],type2:s[a],lineType:s[a-1]};break;case 56:this.$={type1:"none",type2:s[a],lineType:s[a-1]};break;case 57:this.$={type1:s[a-1],type2:"none",lineType:s[a]};break;case 58:this.$={type1:"none",type2:"none",lineType:s[a]};break;case 59:this.$=i.relationType.AGGREGATION;break;case 60:this.$=i.relationType.EXTENSION;break;case 61:this.$=i.relationType.COMPOSITION;break;case 62:this.$=i.relationType.DEPENDENCY;break;case 63:this.$=i.relationType.LOLLIPOP;break;case 64:this.$=i.lineType.LINE;break;case 65:this.$=i.lineType.DOTTED_LINE;break;case 66:case 72:this.$=s[a-2],i.setClickEvent(s[a-1],s[a]);break;case 67:case 73:this.$=s[a-3],i.setClickEvent(s[a-2],s[a-1]),i.setTooltip(s[a-2],s[a]);break;case 68:case 76:this.$=s[a-2],i.setLink(s[a-1],s[a]);break;case 69:case 77:this.$=s[a-3],i.setLink(s[a-2],s[a-1],s[a]);break;case 70:case 78:this.$=s[a-3],i.setLink(s[a-2],s[a-1]),i.setTooltip(s[a-2],s[a]);break;case 71:case 79:this.$=s[a-4],i.setLink(s[a-3],s[a-2],s[a]),i.setTooltip(s[a-3],s[a-1]);break;case 74:this.$=s[a-3],i.setClickEvent(s[a-2],s[a-1],s[a]);break;case 75:this.$=s[a-4],i.setClickEvent(s[a-3],s[a-2],s[a-1]),i.setTooltip(s[a-3],s[a]);break;case 80:i.setCssClass(s[a-1],s[a])}},table:[{3:1,4:2,5:e,6:4,7:5,8:n,9:i,10:u,11:s,12:6,13:11,19:r,23:a},{1:[3]},{1:[2,1]},{1:[2,2]},{1:[2,3]},{3:14,4:2,5:e,6:4,7:5,8:n,9:i,10:u,11:s,12:6,13:11,19:r,23:a},{1:[2,9]},t(o,[2,5]),t(o,[2,6]),t(o,[2,7]),t(o,[2,8]),{14:15,20:[1,16]},{16:[1,17]},{20:[2,12]},{1:[2,4]},{15:18,17:[1,19],22:c},t([17,22],[2,13]),{6:31,7:30,8:n,9:i,10:u,11:s,13:11,19:r,24:21,26:22,27:35,28:46,29:47,31:23,33:24,34:25,35:26,36:27,37:28,38:29,39:l,41:h,43:A,44:p,49:d,51:y,52:E,55:C,57:g,67:F,68:m,70:k,74:f,88:D,89:_,90:B,91:b},{16:[1,52]},{18:53,21:[1,54]},{16:[2,15]},{25:[1,55]},{16:[1,56],25:[2,17]},t(T,[2,25],{32:[1,57]}),t(T,[2,27]),t(T,[2,28]),t(T,[2,29]),t(T,[2,30]),t(T,[2,31]),t(T,[2,32]),t(T,[2,33]),t(T,[2,34]),{40:[1,58]},{42:[1,59]},t(T,[2,37]),t(T,[2,45],{53:60,58:63,59:64,32:[1,62],54:[1,61],60:S,61:v,62:N,63:L,64:$,65:O,66:I}),{27:72,28:46,29:47,88:D,89:_,90:B,91:b},t(T,[2,47]),t(T,[2,48]),{28:73,88:D,89:_,90:B},{27:74,28:46,29:47,88:D,89:_,90:B,91:b},{27:75,28:46,29:47,88:D,89:_,90:B,91:b},{27:76,28:46,29:47,88:D,89:_,90:B,91:b},{54:[1,77]},{27:78,28:46,29:47,88:D,89:_,90:B,91:b},{54:x,56:79},t(R,[2,20],{28:46,29:47,27:81,30:[1,82],88:D,89:_,90:B,91:b}),t(R,[2,21],{30:[1,83]}),t(P,[2,94]),t(P,[2,95]),t(P,[2,96]),t([16,25,30,32,45,46,54,60,61,62,63,64,65,66,71,73],[2,97]),t(w,[2,10]),{15:84,22:c},{22:[2,14]},{1:[2,16]},{6:31,7:30,8:n,9:i,10:u,11:s,13:11,19:r,24:85,25:[2,18],26:22,27:35,28:46,29:47,31:23,33:24,34:25,35:26,36:27,37:28,38:29,39:l,41:h,43:A,44:p,49:d,51:y,52:E,55:C,57:g,67:F,68:m,70:k,74:f,88:D,89:_,90:B,91:b},t(T,[2,26]),t(T,[2,35]),t(T,[2,36]),{27:86,28:46,29:47,54:[1,87],88:D,89:_,90:B,91:b},{53:88,58:63,59:64,60:S,61:v,62:N,63:L,64:$,65:O,66:I},t(T,[2,46]),{59:89,65:O,66:I},t(G,[2,58],{58:90,60:S,61:v,62:N,63:L,64:$}),t(U,[2,59]),t(U,[2,60]),t(U,[2,61]),t(U,[2,62]),t(U,[2,63]),t(M,[2,64]),t(M,[2,65]),t(T,[2,38],{45:[1,91],46:[1,92]}),{50:[1,93]},{54:[1,94]},{54:[1,95]},{71:[1,96],73:[1,97]},{28:98,88:D,89:_,90:B},{54:x,56:99},t(T,[2,54]),t(T,[2,98]),t(R,[2,22]),t(R,[2,23]),t(R,[2,24]),{16:[1,100]},{25:[2,19]},t(Y,[2,49]),{27:101,28:46,29:47,88:D,89:_,90:B,91:b},{27:102,28:46,29:47,54:[1,103],88:D,89:_,90:B,91:b},t(G,[2,57],{58:104,60:S,61:v,62:N,63:L,64:$}),t(G,[2,56]),{28:105,88:D,89:_,90:B},{47:106,51:z},{27:108,28:46,29:47,88:D,89:_,90:B,91:b},t(T,[2,66],{54:[1,109]}),t(T,[2,68],{54:[1,111],69:[1,110]}),t(T,[2,72],{54:[1,112],72:[1,113]}),t(T,[2,76],{54:[1,115],69:[1,114]}),t(T,[2,80]),t(T,[2,53]),t(w,[2,11]),t(Y,[2,51]),t(Y,[2,50]),{27:116,28:46,29:47,88:D,89:_,90:B,91:b},t(G,[2,55]),t(T,[2,39],{46:[1,117]}),{48:[1,118]},{47:119,48:[2,43],51:z},t(T,[2,42]),t(T,[2,67]),t(T,[2,69]),t(T,[2,70],{69:[1,120]}),t(T,[2,73]),t(T,[2,74],{54:[1,121]}),t(T,[2,77]),t(T,[2,78],{69:[1,122]}),t(Y,[2,52]),{47:123,51:z},t(T,[2,40]),{48:[2,44]},t(T,[2,71]),t(T,[2,75]),t(T,[2,79]),{48:[1,124]},t(T,[2,41])],defaultActions:{2:[2,1],3:[2,2],4:[2,3],6:[2,9],13:[2,12],14:[2,4],20:[2,15],54:[2,14],55:[2,16],85:[2,19],119:[2,44]},parseError:function(t,e){if(!e.recoverable){var n=new Error(t);throw n.hash=e,n}this.trace(t)},parse:function(t){var e=this,n=[0],i=[],u=[null],s=[],r=this.table,a="",o=0,c=0,l=2,h=1,A=s.slice.call(arguments,1),p=Object.create(this.lexer),d={yy:{}};for(var y in this.yy)Object.prototype.hasOwnProperty.call(this.yy,y)&&(d.yy[y]=this.yy[y]);p.setInput(t,d.yy),d.yy.lexer=p,d.yy.parser=this,void 0===p.yylloc&&(p.yylloc={});var E=p.yylloc;s.push(E);var C=p.options&&p.options.ranges;function g(){var t;return"number"!=typeof(t=i.pop()||p.lex()||h)&&(t instanceof Array&&(t=(i=t).pop()),t=e.symbols_[t]||t),t}"function"==typeof d.yy.parseError?this.parseError=d.yy.parseError:this.parseError=Object.getPrototypeOf(this).parseError;for(var F,m,k,f,D,_,B,b,T={};;){if(m=n[n.length-1],this.defaultActions[m]?k=this.defaultActions[m]:(null==F&&(F=g()),k=r[m]&&r[m][F]),void 0===k||!k.length||!k[0]){var S="";for(D in b=[],r[m])this.terminals_[D]&&D>l&&b.push("'"+this.terminals_[D]+"'");S=p.showPosition?"Parse error on line "+(o+1)+":\n"+p.showPosition()+"\nExpecting "+b.join(", ")+", got '"+(this.terminals_[F]||F)+"'":"Parse error on line "+(o+1)+": Unexpected "+(F==h?"end of input":"'"+(this.terminals_[F]||F)+"'"),this.parseError(S,{text:p.match,token:this.terminals_[F]||F,line:p.yylineno,loc:E,expected:b})}if(k[0]instanceof Array&&k.length>1)throw new Error("Parse Error: multiple actions possible at state: "+m+", token: "+F);switch(k[0]){case 1:n.push(F),u.push(p.yytext),s.push(p.yylloc),n.push(k[1]),F=null,c=p.yyleng,a=p.yytext,o=p.yylineno,E=p.yylloc;break;case 2:if(_=this.productions_[k[1]][1],T.$=u[u.length-_],T._$={first_line:s[s.length-(_||1)].first_line,last_line:s[s.length-1].last_line,first_column:s[s.length-(_||1)].first_column,last_column:s[s.length-1].last_column},C&&(T._$.range=[s[s.length-(_||1)].range[0],s[s.length-1].range[1]]),void 0!==(f=this.performAction.apply(T,[a,c,o,d.yy,k[1],u,s].concat(A))))return f;_&&(n=n.slice(0,-1*_*2),u=u.slice(0,-1*_),s=s.slice(0,-1*_)),n.push(this.productions_[k[1]][0]),u.push(T.$),s.push(T._$),B=r[n[n.length-2]][n[n.length-1]],n.push(B);break;case 3:return!0}}return!0}},j={EOF:1,parseError:function(t,e){if(!this.yy.parser)throw new Error(t);this.yy.parser.parseError(t,e)},setInput:function(t,e){return this.yy=e||this.yy||{},this._input=t,this._more=this._backtrack=this.done=!1,this.yylineno=this.yyleng=0,this.yytext=this.matched=this.match="",this.conditionStack=["INITIAL"],this.yylloc={first_line:1,first_column:0,last_line:1,last_column:0},this.options.ranges&&(this.yylloc.range=[0,0]),this.offset=0,this},input:function(){var t=this._input[0];return this.yytext+=t,this.yyleng++,this.offset++,this.match+=t,this.matched+=t,t.match(/(?:\r\n?|\n).*/g)?(this.yylineno++,this.yylloc.last_line++):this.yylloc.last_column++,this.options.ranges&&this.yylloc.range[1]++,this._input=this._input.slice(1),t},unput:function(t){var e=t.length,n=t.split(/(?:\r\n?|\n)/g);this._input=t+this._input,this.yytext=this.yytext.substr(0,this.yytext.length-e),this.offset-=e;var i=this.match.split(/(?:\r\n?|\n)/g);this.match=this.match.substr(0,this.match.length-1),this.matched=this.matched.substr(0,this.matched.length-1),n.length-1&&(this.yylineno-=n.length-1);var u=this.yylloc.range;return this.yylloc={first_line:this.yylloc.first_line,last_line:this.yylineno+1,first_column:this.yylloc.first_column,last_column:n?(n.length===i.length?this.yylloc.first_column:0)+i[i.length-n.length].length-n[0].length:this.yylloc.first_column-e},this.options.ranges&&(this.yylloc.range=[u[0],u[0]+this.yyleng-e]),this.yyleng=this.yytext.length,this},more:function(){return this._more=!0,this},reject:function(){return this.options.backtrack_lexer?(this._backtrack=!0,this):this.parseError("Lexical error on line "+(this.yylineno+1)+". You can only invoke reject() in the lexer when the lexer is of the backtracking persuasion (options.backtrack_lexer = true).\n"+this.showPosition(),{text:"",token:null,line:this.yylineno})},less:function(t){this.unput(this.match.slice(t))},pastInput:function(){var t=this.matched.substr(0,this.matched.length-this.match.length);return(t.length>20?"...":"")+t.substr(-20).replace(/\n/g,"")},upcomingInput:function(){var t=this.match;return t.length<20&&(t+=this._input.substr(0,20-t.length)),(t.substr(0,20)+(t.length>20?"...":"")).replace(/\n/g,"")},showPosition:function(){var t=this.pastInput(),e=new Array(t.length+1).join("-");return t+this.upcomingInput()+"\n"+e+"^"},test_match:function(t,e){var n,i,u;if(this.options.backtrack_lexer&&(u={yylineno:this.yylineno,yylloc:{first_line:this.yylloc.first_line,last_line:this.last_line,first_column:this.yylloc.first_column,last_column:this.yylloc.last_column},yytext:this.yytext,match:this.match,matches:this.matches,matched:this.matched,yyleng:this.yyleng,offset:this.offset,_more:this._more,_input:this._input,yy:this.yy,conditionStack:this.conditionStack.slice(0),done:this.done},this.options.ranges&&(u.yylloc.range=this.yylloc.range.slice(0))),(i=t[0].match(/(?:\r\n?|\n).*/g))&&(this.yylineno+=i.length),this.yylloc={first_line:this.yylloc.last_line,last_line:this.yylineno+1,first_column:this.yylloc.last_column,last_column:i?i[i.length-1].length-i[i.length-1].match(/\r?\n?/)[0].length:this.yylloc.last_column+t[0].length},this.yytext+=t[0],this.match+=t[0],this.matches=t,this.yyleng=this.yytext.length,this.options.ranges&&(this.yylloc.range=[this.offset,this.offset+=this.yyleng]),this._more=!1,this._backtrack=!1,this._input=this._input.slice(t[0].length),this.matched+=t[0],n=this.performAction.call(this,this.yy,this,e,this.conditionStack[this.conditionStack.length-1]),this.done&&this._input&&(this.done=!1),n)return n;if(this._backtrack){for(var s in u)this[s]=u[s];return!1}return!1},next:function(){if(this.done)return this.EOF;var t,e,n,i;this._input||(this.done=!0),this._more||(this.yytext="",this.match="");for(var u=this._currentRules(),s=0;s<u.length;s++)if((n=this._input.match(this.rules[u[s]]))&&(!e||n[0].length>e[0].length)){if(e=n,i=s,this.options.backtrack_lexer){if(!1!==(t=this.test_match(n,u[s])))return t;if(this._backtrack){e=!1;continue}return!1}if(!this.options.flex)break}return e?!1!==(t=this.test_match(e,u[i]))&&t:""===this._input?this.EOF:this.parseError("Lexical error on line "+(this.yylineno+1)+". Unrecognized text.\n"+this.showPosition(),{text:"",token:null,line:this.yylineno})},lex:function(){var t=this.next();return t||this.lex()},begin:function(t){this.conditionStack.push(t)},popState:function(){return this.conditionStack.length-1>0?this.conditionStack.pop():this.conditionStack[0]},_currentRules:function(){return this.conditionStack.length&&this.conditionStack[this.conditionStack.length-1]?this.conditions[this.conditionStack[this.conditionStack.length-1]].rules:this.conditions.INITIAL.rules},topState:function(t){return(t=this.conditionStack.length-1-Math.abs(t||0))>=0?this.conditionStack[t]:"INITIAL"},pushState:function(t){this.begin(t)},stateStackSize:function(){return this.conditionStack.length},options:{},performAction:function(t,e,n,i){switch(n){case 0:return this.begin("open_directive"),19;case 1:return 8;case 2:return 9;case 3:return 10;case 4:return 11;case 5:return this.begin("type_directive"),20;case 6:return this.popState(),this.begin("arg_directive"),17;case 7:return this.popState(),this.popState(),22;case 8:return 21;case 9:case 10:case 19:case 27:break;case 11:return this.begin("acc_title"),39;case 12:return this.popState(),"acc_title_value";case 13:return this.begin("acc_descr"),41;case 14:return this.popState(),"acc_descr_value";case 15:this.begin("acc_descr_multiline");break;case 16:case 39:case 42:case 45:case 48:case 51:case 54:this.popState();break;case 17:return"acc_descr_multiline_value";case 18:return 16;case 20:case 21:return 23;case 22:return this.begin("struct"),46;case 23:return"EDGE_STATE";case 24:return"EOF_IN_STRUCT";case 25:return"OPEN_IN_STRUCT";case 26:return this.popState(),48;case 28:return"MEMBER";case 29:return 44;case 30:return 74;case 31:return 67;case 32:return 68;case 33:return 70;case 34:return 55;case 35:return 57;case 36:return 49;case 37:return 50;case 38:this.begin("generic");break;case 40:return"GENERICTYPE";case 41:this.begin("string");break;case 43:return"STR";case 44:this.begin("bqstring");break;case 46:return"BQUOTE_STR";case 47:this.begin("href");break;case 49:return 73;case 50:this.begin("callback_name");break;case 52:this.popState(),this.begin("callback_args");break;case 53:return 71;case 55:return 72;case 56:case 57:case 58:case 59:return 69;case 60:case 61:return 61;case 62:case 63:return 63;case 64:return 62;case 65:return 60;case 66:return 64;case 67:return 65;case 68:return 66;case 69:return 32;case 70:return 45;case 71:return 86;case 72:return"DOT";case 73:return"PLUS";case 74:return 83;case 75:case 76:return"EQUALS";case 77:return 90;case 78:return"PUNCTUATION";case 79:return 89;case 80:return 88;case 81:return 85;case 82:return 25}},rules:[/^(?:%%\{)/,/^(?:.*direction\s+TB[^\n]*)/,/^(?:.*direction\s+BT[^\n]*)/,/^(?:.*direction\s+RL[^\n]*)/,/^(?:.*direction\s+LR[^\n]*)/,/^(?:((?:(?!\}%%)[^:.])*))/,/^(?::)/,/^(?:\}%%)/,/^(?:((?:(?!\}%%).|\n)*))/,/^(?:%%(?!\{)*[^\n]*(\r?\n?)+)/,/^(?:%%[^\n]*(\r?\n)*)/,/^(?:accTitle\s*:\s*)/,/^(?:(?!\n||)*[^\n]*)/,/^(?:accDescr\s*:\s*)/,/^(?:(?!\n||)*[^\n]*)/,/^(?:accDescr\s*\{\s*)/,/^(?:[\}])/,/^(?:[^\}]*)/,/^(?:\s*(\r?\n)+)/,/^(?:\s+)/,/^(?:classDiagram-v2\b)/,/^(?:classDiagram\b)/,/^(?:[{])/,/^(?:\[\*\])/,/^(?:$)/,/^(?:[{])/,/^(?:[}])/,/^(?:[\n])/,/^(?:[^{}\n]*)/,/^(?:class\b)/,/^(?:cssClass\b)/,/^(?:callback\b)/,/^(?:link\b)/,/^(?:click\b)/,/^(?:note for\b)/,/^(?:note\b)/,/^(?:<<)/,/^(?:>>)/,/^(?:[~])/,/^(?:[~])/,/^(?:[^~]*)/,/^(?:["])/,/^(?:["])/,/^(?:[^"]*)/,/^(?:[`])/,/^(?:[`])/,/^(?:[^`]+)/,/^(?:href[\s]+["])/,/^(?:["])/,/^(?:[^"]*)/,/^(?:call[\s]+)/,/^(?:\([\s]*\))/,/^(?:\()/,/^(?:[^(]*)/,/^(?:\))/,/^(?:[^)]*)/,/^(?:_self\b)/,/^(?:_blank\b)/,/^(?:_parent\b)/,/^(?:_top\b)/,/^(?:\s*<\|)/,/^(?:\s*\|>)/,/^(?:\s*>)/,/^(?:\s*<)/,/^(?:\s*\*)/,/^(?:\s*o\b)/,/^(?:\s*\(\))/,/^(?:--)/,/^(?:\.\.)/,/^(?::{1}[^:\n;]+)/,/^(?::{3})/,/^(?:-)/,/^(?:\.)/,/^(?:\+)/,/^(?:%)/,/^(?:=)/,/^(?:=)/,/^(?:\w+)/,/^(?:[!"#$%&'*+,-.`?\\/])/,/^(?:[0-9]+)/,/^(?:[\u00AA\u00B5\u00BA\u00C0-\u00D6\u00D8-\u00F6]|[\u00F8-\u02C1\u02C6-\u02D1\u02E0-\u02E4\u02EC\u02EE\u0370-\u0374\u0376\u0377]|[\u037A-\u037D\u0386\u0388-\u038A\u038C\u038E-\u03A1\u03A3-\u03F5]|[\u03F7-\u0481\u048A-\u0527\u0531-\u0556\u0559\u0561-\u0587\u05D0-\u05EA]|[\u05F0-\u05F2\u0620-\u064A\u066E\u066F\u0671-\u06D3\u06D5\u06E5\u06E6\u06EE]|[\u06EF\u06FA-\u06FC\u06FF\u0710\u0712-\u072F\u074D-\u07A5\u07B1\u07CA-\u07EA]|[\u07F4\u07F5\u07FA\u0800-\u0815\u081A\u0824\u0828\u0840-\u0858\u08A0]|[\u08A2-\u08AC\u0904-\u0939\u093D\u0950\u0958-\u0961\u0971-\u0977]|[\u0979-\u097F\u0985-\u098C\u098F\u0990\u0993-\u09A8\u09AA-\u09B0\u09B2]|[\u09B6-\u09B9\u09BD\u09CE\u09DC\u09DD\u09DF-\u09E1\u09F0\u09F1\u0A05-\u0A0A]|[\u0A0F\u0A10\u0A13-\u0A28\u0A2A-\u0A30\u0A32\u0A33\u0A35\u0A36\u0A38\u0A39]|[\u0A59-\u0A5C\u0A5E\u0A72-\u0A74\u0A85-\u0A8D\u0A8F-\u0A91\u0A93-\u0AA8]|[\u0AAA-\u0AB0\u0AB2\u0AB3\u0AB5-\u0AB9\u0ABD\u0AD0\u0AE0\u0AE1\u0B05-\u0B0C]|[\u0B0F\u0B10\u0B13-\u0B28\u0B2A-\u0B30\u0B32\u0B33\u0B35-\u0B39\u0B3D\u0B5C]|[\u0B5D\u0B5F-\u0B61\u0B71\u0B83\u0B85-\u0B8A\u0B8E-\u0B90\u0B92-\u0B95\u0B99]|[\u0B9A\u0B9C\u0B9E\u0B9F\u0BA3\u0BA4\u0BA8-\u0BAA\u0BAE-\u0BB9\u0BD0]|[\u0C05-\u0C0C\u0C0E-\u0C10\u0C12-\u0C28\u0C2A-\u0C33\u0C35-\u0C39\u0C3D]|[\u0C58\u0C59\u0C60\u0C61\u0C85-\u0C8C\u0C8E-\u0C90\u0C92-\u0CA8\u0CAA-\u0CB3]|[\u0CB5-\u0CB9\u0CBD\u0CDE\u0CE0\u0CE1\u0CF1\u0CF2\u0D05-\u0D0C\u0D0E-\u0D10]|[\u0D12-\u0D3A\u0D3D\u0D4E\u0D60\u0D61\u0D7A-\u0D7F\u0D85-\u0D96\u0D9A-\u0DB1]|[\u0DB3-\u0DBB\u0DBD\u0DC0-\u0DC6\u0E01-\u0E30\u0E32\u0E33\u0E40-\u0E46\u0E81]|[\u0E82\u0E84\u0E87\u0E88\u0E8A\u0E8D\u0E94-\u0E97\u0E99-\u0E9F\u0EA1-\u0EA3]|[\u0EA5\u0EA7\u0EAA\u0EAB\u0EAD-\u0EB0\u0EB2\u0EB3\u0EBD\u0EC0-\u0EC4\u0EC6]|[\u0EDC-\u0EDF\u0F00\u0F40-\u0F47\u0F49-\u0F6C\u0F88-\u0F8C\u1000-\u102A]|[\u103F\u1050-\u1055\u105A-\u105D\u1061\u1065\u1066\u106E-\u1070\u1075-\u1081]|[\u108E\u10A0-\u10C5\u10C7\u10CD\u10D0-\u10FA\u10FC-\u1248\u124A-\u124D]|[\u1250-\u1256\u1258\u125A-\u125D\u1260-\u1288\u128A-\u128D\u1290-\u12B0]|[\u12B2-\u12B5\u12B8-\u12BE\u12C0\u12C2-\u12C5\u12C8-\u12D6\u12D8-\u1310]|[\u1312-\u1315\u1318-\u135A\u1380-\u138F\u13A0-\u13F4\u1401-\u166C]|[\u166F-\u167F\u1681-\u169A\u16A0-\u16EA\u1700-\u170C\u170E-\u1711]|[\u1720-\u1731\u1740-\u1751\u1760-\u176C\u176E-\u1770\u1780-\u17B3\u17D7]|[\u17DC\u1820-\u1877\u1880-\u18A8\u18AA\u18B0-\u18F5\u1900-\u191C]|[\u1950-\u196D\u1970-\u1974\u1980-\u19AB\u19C1-\u19C7\u1A00-\u1A16]|[\u1A20-\u1A54\u1AA7\u1B05-\u1B33\u1B45-\u1B4B\u1B83-\u1BA0\u1BAE\u1BAF]|[\u1BBA-\u1BE5\u1C00-\u1C23\u1C4D-\u1C4F\u1C5A-\u1C7D\u1CE9-\u1CEC]|[\u1CEE-\u1CF1\u1CF5\u1CF6\u1D00-\u1DBF\u1E00-\u1F15\u1F18-\u1F1D]|[\u1F20-\u1F45\u1F48-\u1F4D\u1F50-\u1F57\u1F59\u1F5B\u1F5D\u1F5F-\u1F7D]|[\u1F80-\u1FB4\u1FB6-\u1FBC\u1FBE\u1FC2-\u1FC4\u1FC6-\u1FCC\u1FD0-\u1FD3]|[\u1FD6-\u1FDB\u1FE0-\u1FEC\u1FF2-\u1FF4\u1FF6-\u1FFC\u2071\u207F]|[\u2090-\u209C\u2102\u2107\u210A-\u2113\u2115\u2119-\u211D\u2124\u2126\u2128]|[\u212A-\u212D\u212F-\u2139\u213C-\u213F\u2145-\u2149\u214E\u2183\u2184]|[\u2C00-\u2C2E\u2C30-\u2C5E\u2C60-\u2CE4\u2CEB-\u2CEE\u2CF2\u2CF3]|[\u2D00-\u2D25\u2D27\u2D2D\u2D30-\u2D67\u2D6F\u2D80-\u2D96\u2DA0-\u2DA6]|[\u2DA8-\u2DAE\u2DB0-\u2DB6\u2DB8-\u2DBE\u2DC0-\u2DC6\u2DC8-\u2DCE]|[\u2DD0-\u2DD6\u2DD8-\u2DDE\u2E2F\u3005\u3006\u3031-\u3035\u303B\u303C]|[\u3041-\u3096\u309D-\u309F\u30A1-\u30FA\u30FC-\u30FF\u3105-\u312D]|[\u3131-\u318E\u31A0-\u31BA\u31F0-\u31FF\u3400-\u4DB5\u4E00-\u9FCC]|[\uA000-\uA48C\uA4D0-\uA4FD\uA500-\uA60C\uA610-\uA61F\uA62A\uA62B]|[\uA640-\uA66E\uA67F-\uA697\uA6A0-\uA6E5\uA717-\uA71F\uA722-\uA788]|[\uA78B-\uA78E\uA790-\uA793\uA7A0-\uA7AA\uA7F8-\uA801\uA803-\uA805]|[\uA807-\uA80A\uA80C-\uA822\uA840-\uA873\uA882-\uA8B3\uA8F2-\uA8F7\uA8FB]|[\uA90A-\uA925\uA930-\uA946\uA960-\uA97C\uA984-\uA9B2\uA9CF\uAA00-\uAA28]|[\uAA40-\uAA42\uAA44-\uAA4B\uAA60-\uAA76\uAA7A\uAA80-\uAAAF\uAAB1\uAAB5]|[\uAAB6\uAAB9-\uAABD\uAAC0\uAAC2\uAADB-\uAADD\uAAE0-\uAAEA\uAAF2-\uAAF4]|[\uAB01-\uAB06\uAB09-\uAB0E\uAB11-\uAB16\uAB20-\uAB26\uAB28-\uAB2E]|[\uABC0-\uABE2\uAC00-\uD7A3\uD7B0-\uD7C6\uD7CB-\uD7FB\uF900-\uFA6D]|[\uFA70-\uFAD9\uFB00-\uFB06\uFB13-\uFB17\uFB1D\uFB1F-\uFB28\uFB2A-\uFB36]|[\uFB38-\uFB3C\uFB3E\uFB40\uFB41\uFB43\uFB44\uFB46-\uFBB1\uFBD3-\uFD3D]|[\uFD50-\uFD8F\uFD92-\uFDC7\uFDF0-\uFDFB\uFE70-\uFE74\uFE76-\uFEFC]|[\uFF21-\uFF3A\uFF41-\uFF5A\uFF66-\uFFBE\uFFC2-\uFFC7\uFFCA-\uFFCF]|[\uFFD2-\uFFD7\uFFDA-\uFFDC])/,/^(?:\s)/,/^(?:$)/],conditions:{acc_descr_multiline:{rules:[16,17],inclusive:!1},acc_descr:{rules:[14],inclusive:!1},acc_title:{rules:[12],inclusive:!1},arg_directive:{rules:[7,8],inclusive:!1},type_directive:{rules:[6,7],inclusive:!1},open_directive:{rules:[5],inclusive:!1},callback_args:{rules:[54,55],inclusive:!1},callback_name:{rules:[51,52,53],inclusive:!1},href:{rules:[48,49],inclusive:!1},struct:{rules:[23,24,25,26,27,28],inclusive:!1},generic:{rules:[39,40],inclusive:!1},bqstring:{rules:[45,46],inclusive:!1},string:{rules:[42,43],inclusive:!1},INITIAL:{rules:[0,1,2,3,4,9,10,11,13,15,18,19,20,21,22,23,29,30,31,32,33,34,35,36,37,38,41,44,47,50,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82],inclusive:!0}}};function X(){this.yy={}}return K.lexer=j,X.prototype=K,K.Parser=X,new X}();o.parser=o;const c=o,l="classid-";let h=[],A={},p=[],d=0,y=[];const E=t=>u.d.sanitizeText(t,(0,u.g)()),C=function(t){let e="",n=t;if(t.indexOf("~")>0){let i=t.split("~");n=i[0],e=u.d.sanitizeText(i[1],(0,u.g)())}return{className:n,type:e}},g=function(t){let e=C(t);void 0===A[e.className]&&(A[e.className]={id:e.className,type:e.type,cssClasses:[],methods:[],members:[],annotations:[],domId:l+e.className+"-"+d},d++)},F=function(t){const e=Object.keys(A);for(const n of e)if(A[n].id===t)return A[n].domId},m=function(t,e){const n=C(t).className,i=A[n];if("string"==typeof e){const t=e.trim();t.startsWith("<<")&&t.endsWith(">>")?i.annotations.push(E(t.substring(2,t.length-2))):t.indexOf(")")>0?i.methods.push(E(t)):t&&i.members.push(E(t))}},k=function(t,e){t.split(",").forEach((function(t){let n=t;t[0].match(/\d/)&&(n=l+n),void 0!==A[n]&&A[n].cssClasses.push(e)}))},f=function(t,e,n){const i=(0,u.g)();let r=t,a=F(r);if("loose"===i.securityLevel&&void 0!==e&&void 0!==A[r]){let t=[];if("string"==typeof n){t=n.split(/,(?=(?:(?:[^"]*"){2})*[^"]*$)/);for(let e=0;e<t.length;e++){let n=t[e].trim();'"'===n.charAt(0)&&'"'===n.charAt(n.length-1)&&(n=n.substr(1,n.length-2)),t[e]=n}}0===t.length&&t.push(a),y.push((function(){const n=document.querySelector(`[id="${a}"]`);null!==n&&n.addEventListener("click",(function(){s.u.runFunc(e,...t)}),!1)}))}},D=function(t){let e=(0,i.Ys)(".mermaidTooltip");null===(e._groups||e)[0][0]&&(e=(0,i.Ys)("body").append("div").attr("class","mermaidTooltip").style("opacity",0));(0,i.Ys)(t).select("svg").selectAll("g.node").on("mouseover",(function(){const t=(0,i.Ys)(this);if(null===t.attr("title"))return;const n=this.getBoundingClientRect();e.transition().duration(200).style("opacity",".9"),e.text(t.attr("title")).style("left",window.scrollX+n.left+(n.right-n.left)/2+"px").style("top",window.scrollY+n.top-14+document.body.scrollTop+"px"),e.html(e.html().replace(/&lt;br\/&gt;/g,"<br/>")),t.classed("hover",!0)})).on("mouseout",(function(){e.transition().duration(500).style("opacity",0);(0,i.Ys)(this).classed("hover",!1)}))};y.push(D);let _="TB";const B={parseDirective:function(t,e,n){r.m.parseDirective(this,t,e,n)},setAccTitle:a.s,getAccTitle:a.g,getAccDescription:a.a,setAccDescription:a.b,getConfig:()=>(0,u.g)().class,addClass:g,bindFunctions:function(t){y.forEach((function(e){e(t)}))},clear:function(){h=[],A={},p=[],y=[],y.push(D),(0,a.f)()},getClass:function(t){return A[t]},getClasses:function(){return A},getNotes:function(){return p},addAnnotation:function(t,e){const n=C(t).className;A[n].annotations.push(e)},addNote:function(t,e){const n={id:`note${p.length}`,class:e,text:t};p.push(n)},getRelations:function(){return h},addRelation:function(t){u.l.debug("Adding relation: "+JSON.stringify(t)),g(t.id1),g(t.id2),t.id1=C(t.id1).className,t.id2=C(t.id2).className,t.relationTitle1=u.d.sanitizeText(t.relationTitle1.trim(),(0,u.g)()),t.relationTitle2=u.d.sanitizeText(t.relationTitle2.trim(),(0,u.g)()),h.push(t)},getDirection:()=>_,setDirection:t=>{_=t},addMember:m,addMembers:function(t,e){Array.isArray(e)&&(e.reverse(),e.forEach((e=>m(t,e))))},cleanupLabel:function(t){return":"===t.substring(0,1)?u.d.sanitizeText(t.substr(1).trim(),(0,u.g)()):E(t.trim())},lineType:{LINE:0,DOTTED_LINE:1},relationType:{AGGREGATION:0,EXTENSION:1,COMPOSITION:2,DEPENDENCY:3,LOLLIPOP:4},setClickEvent:function(t,e,n){t.split(",").forEach((function(t){f(t,e,n),A[t].haveCallback=!0})),k(t,"clickable")},setCssClass:k,setLink:function(t,e,n){const i=(0,u.g)();t.split(",").forEach((function(t){let u=t;t[0].match(/\d/)&&(u=l+u),void 0!==A[u]&&(A[u].link=s.u.formatUrl(e,i),"sandbox"===i.securityLevel?A[u].linkTarget="_top":A[u].linkTarget="string"==typeof n?E(n):"_blank")})),k(t,"clickable")},getTooltip:function(t){return A[t].tooltip},setTooltip:function(t,e){const n=(0,u.g)();t.split(",").forEach((function(t){void 0!==e&&(A[t].tooltip=u.d.sanitizeText(e,n))}))},lookUpDomId:F,setDiagramTitle:a.d,getDiagramTitle:a.e},b=t=>`g.classGroup text {\n  fill: ${t.nodeBorder};\n  fill: ${t.classText};\n  stroke: none;\n  font-family: ${t.fontFamily};\n  font-size: 10px;\n\n  .title {\n    font-weight: bolder;\n  }\n\n}\n\n.nodeLabel, .edgeLabel {\n  color: ${t.classText};\n}\n.edgeLabel .label rect {\n  fill: ${t.mainBkg};\n}\n.label text {\n  fill: ${t.classText};\n}\n.edgeLabel .label span {\n  background: ${t.mainBkg};\n}\n\n.classTitle {\n  font-weight: bolder;\n}\n.node rect,\n  .node circle,\n  .node ellipse,\n  .node polygon,\n  .node path {\n    fill: ${t.mainBkg};\n    stroke: ${t.nodeBorder};\n    stroke-width: 1px;\n  }\n\n\n.divider {\n  stroke: ${t.nodeBorder};\n  stroke: 1;\n}\n\ng.clickable {\n  cursor: pointer;\n}\n\ng.classGroup rect {\n  fill: ${t.mainBkg};\n  stroke: ${t.nodeBorder};\n}\n\ng.classGroup line {\n  stroke: ${t.nodeBorder};\n  stroke-width: 1;\n}\n\n.classLabel .box {\n  stroke: none;\n  stroke-width: 0;\n  fill: ${t.mainBkg};\n  opacity: 0.5;\n}\n\n.classLabel .label {\n  fill: ${t.nodeBorder};\n  font-size: 10px;\n}\n\n.relation {\n  stroke: ${t.lineColor};\n  stroke-width: 1;\n  fill: none;\n}\n\n.dashed-line{\n  stroke-dasharray: 3;\n}\n\n.dotted-line{\n  stroke-dasharray: 1 2;\n}\n\n#compositionStart, .composition {\n  fill: ${t.lineColor} !important;\n  stroke: ${t.lineColor} !important;\n  stroke-width: 1;\n}\n\n#compositionEnd, .composition {\n  fill: ${t.lineColor} !important;\n  stroke: ${t.lineColor} !important;\n  stroke-width: 1;\n}\n\n#dependencyStart, .dependency {\n  fill: ${t.lineColor} !important;\n  stroke: ${t.lineColor} !important;\n  stroke-width: 1;\n}\n\n#dependencyStart, .dependency {\n  fill: ${t.lineColor} !important;\n  stroke: ${t.lineColor} !important;\n  stroke-width: 1;\n}\n\n#extensionStart, .extension {\n  fill: ${t.mainBkg} !important;\n  stroke: ${t.lineColor} !important;\n  stroke-width: 1;\n}\n\n#extensionEnd, .extension {\n  fill: ${t.mainBkg} !important;\n  stroke: ${t.lineColor} !important;\n  stroke-width: 1;\n}\n\n#aggregationStart, .aggregation {\n  fill: ${t.mainBkg} !important;\n  stroke: ${t.lineColor} !important;\n  stroke-width: 1;\n}\n\n#aggregationEnd, .aggregation {\n  fill: ${t.mainBkg} !important;\n  stroke: ${t.lineColor} !important;\n  stroke-width: 1;\n}\n\n#lollipopStart, .lollipop {\n  fill: ${t.mainBkg} !important;\n  stroke: ${t.lineColor} !important;\n  stroke-width: 1;\n}\n\n#lollipopEnd, .lollipop {\n  fill: ${t.mainBkg} !important;\n  stroke: ${t.lineColor} !important;\n  stroke-width: 1;\n}\n\n.edgeTerminals {\n  font-size: 11px;\n}\n\n.classTitleText {\n  text-anchor: middle;\n  font-size: 18px;\n  fill: ${t.textColor};\n}\n`}}]);