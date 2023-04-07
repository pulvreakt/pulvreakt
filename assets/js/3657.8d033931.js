"use strict";(self.webpackChunkdocsite=self.webpackChunkdocsite||[]).push([[3657],{3657:(t,e,i)=>{i.r(e),i.d(e,{diagram:()=>L});var r=i(44),n=i(3047),a=i(5625),s=i(4646),o=i(2494),c=i(1188);const l=[];for(let D=0;D<256;++D)l.push((D+256).toString(16).slice(1));function h(t,e=0){return(l[t[e+0]]+l[t[e+1]]+l[t[e+2]]+l[t[e+3]]+"-"+l[t[e+4]]+l[t[e+5]]+"-"+l[t[e+6]]+l[t[e+7]]+"-"+l[t[e+8]]+l[t[e+9]]+"-"+l[t[e+10]]+l[t[e+11]]+l[t[e+12]]+l[t[e+13]]+l[t[e+14]]+l[t[e+15]]).toLowerCase()}const d=/^(?:[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}|00000000-0000-0000-0000-000000000000)$/i;const y=function(t){return"string"==typeof t&&d.test(t)};const u=function(t){if(!y(t))throw TypeError("Invalid UUID");let e;const i=new Uint8Array(16);return i[0]=(e=parseInt(t.slice(0,8),16))>>>24,i[1]=e>>>16&255,i[2]=e>>>8&255,i[3]=255&e,i[4]=(e=parseInt(t.slice(9,13),16))>>>8,i[5]=255&e,i[6]=(e=parseInt(t.slice(14,18),16))>>>8,i[7]=255&e,i[8]=(e=parseInt(t.slice(19,23),16))>>>8,i[9]=255&e,i[10]=(e=parseInt(t.slice(24,36),16))/1099511627776&255,i[11]=e/4294967296&255,i[12]=e>>>24&255,i[13]=e>>>16&255,i[14]=e>>>8&255,i[15]=255&e,i};function p(t,e,i,r){switch(t){case 0:return e&i^~e&r;case 1:case 3:return e^i^r;case 2:return e&i^e&r^i&r}}function _(t,e){return t<<e|t>>>32-e}const f=function(t,e,i){function r(t,r,n,a){var s;if("string"==typeof t&&(t=function(t){t=unescape(encodeURIComponent(t));const e=[];for(let i=0;i<t.length;++i)e.push(t.charCodeAt(i));return e}(t)),"string"==typeof r&&(r=u(r)),16!==(null===(s=r)||void 0===s?void 0:s.length))throw TypeError("Namespace must be array-like (16 iterable integer values, 0-255)");let o=new Uint8Array(16+t.length);if(o.set(r),o.set(t,r.length),o=i(o),o[6]=15&o[6]|e,o[8]=63&o[8]|128,n){a=a||0;for(let t=0;t<16;++t)n[a+t]=o[t];return n}return h(o)}try{r.name=t}catch(n){}return r.DNS="6ba7b810-9dad-11d1-80b4-00c04fd430c8",r.URL="6ba7b811-9dad-11d1-80b4-00c04fd430c8",r}("v5",80,(function(t){const e=[1518500249,1859775393,2400959708,3395469782],i=[1732584193,4023233417,2562383102,271733878,3285377520];if("string"==typeof t){const e=unescape(encodeURIComponent(t));t=[];for(let i=0;i<e.length;++i)t.push(e.charCodeAt(i))}else Array.isArray(t)||(t=Array.prototype.slice.call(t));t.push(128);const r=t.length/4+2,n=Math.ceil(r/16),a=new Array(n);for(let s=0;s<n;++s){const e=new Uint32Array(16);for(let i=0;i<16;++i)e[i]=t[64*s+4*i]<<24|t[64*s+4*i+1]<<16|t[64*s+4*i+2]<<8|t[64*s+4*i+3];a[s]=e}a[n-1][14]=8*(t.length-1)/Math.pow(2,32),a[n-1][14]=Math.floor(a[n-1][14]),a[n-1][15]=8*(t.length-1)&4294967295;for(let s=0;s<n;++s){const t=new Uint32Array(80);for(let e=0;e<16;++e)t[e]=a[s][e];for(let e=16;e<80;++e)t[e]=_(t[e-3]^t[e-8]^t[e-14]^t[e-16],1);let r=i[0],n=i[1],o=i[2],c=i[3],l=i[4];for(let i=0;i<80;++i){const a=Math.floor(i/20),s=_(r,5)+p(a,n,o,c)+l+e[a]+t[i]>>>0;l=c,c=o,o=_(n,30)>>>0,n=r,r=s}i[0]=i[0]+r>>>0,i[1]=i[1]+n>>>0,i[2]=i[2]+o>>>0,i[3]=i[3]+c>>>0,i[4]=i[4]+l>>>0}return[i[0]>>24&255,i[0]>>16&255,i[0]>>8&255,255&i[0],i[1]>>24&255,i[1]>>16&255,i[1]>>8&255,255&i[1],i[2]>>24&255,i[2]>>16&255,i[2]>>8&255,255&i[2],i[3]>>24&255,i[3]>>16&255,i[3]>>8&255,255&i[3],i[4]>>24&255,i[4]>>16&255,i[4]>>8&255,255&i[4]]}));i(7856),i(7484),i(7967);var g=function(){var t=function(t,e,i,r){for(i=i||{},r=t.length;r--;i[t[r]]=e);return i},e=[1,2],i=[1,5],r=[6,9,11,23,25,27,29,30,31,51],n=[1,17],a=[1,18],s=[1,19],o=[1,20],c=[1,21],l=[1,22],h=[1,25],d=[1,30],y=[1,31],u=[1,32],p=[1,33],_=[6,9,11,15,20,23,25,27,29,30,31,44,45,46,47,51],f=[1,45],g=[30,31,48,49],m=[4,6,9,11,23,25,27,29,30,31,51],O=[44,45,46,47],E=[22,37],b=[1,65],k=[1,64],R=[22,37,39,41],N={trace:function(){},yy:{},symbols_:{error:2,start:3,ER_DIAGRAM:4,document:5,EOF:6,directive:7,line:8,SPACE:9,statement:10,NEWLINE:11,openDirective:12,typeDirective:13,closeDirective:14,":":15,argDirective:16,entityName:17,relSpec:18,role:19,BLOCK_START:20,attributes:21,BLOCK_STOP:22,title:23,title_value:24,acc_title:25,acc_title_value:26,acc_descr:27,acc_descr_value:28,acc_descr_multiline_value:29,ALPHANUM:30,ENTITY_NAME:31,attribute:32,attributeType:33,attributeName:34,attributeKeyTypeList:35,attributeComment:36,ATTRIBUTE_WORD:37,attributeKeyType:38,COMMA:39,ATTRIBUTE_KEY:40,COMMENT:41,cardinality:42,relType:43,ZERO_OR_ONE:44,ZERO_OR_MORE:45,ONE_OR_MORE:46,ONLY_ONE:47,NON_IDENTIFYING:48,IDENTIFYING:49,WORD:50,open_directive:51,type_directive:52,arg_directive:53,close_directive:54,$accept:0,$end:1},terminals_:{2:"error",4:"ER_DIAGRAM",6:"EOF",9:"SPACE",11:"NEWLINE",15:":",20:"BLOCK_START",22:"BLOCK_STOP",23:"title",24:"title_value",25:"acc_title",26:"acc_title_value",27:"acc_descr",28:"acc_descr_value",29:"acc_descr_multiline_value",30:"ALPHANUM",31:"ENTITY_NAME",37:"ATTRIBUTE_WORD",39:"COMMA",40:"ATTRIBUTE_KEY",41:"COMMENT",44:"ZERO_OR_ONE",45:"ZERO_OR_MORE",46:"ONE_OR_MORE",47:"ONLY_ONE",48:"NON_IDENTIFYING",49:"IDENTIFYING",50:"WORD",51:"open_directive",52:"type_directive",53:"arg_directive",54:"close_directive"},productions_:[0,[3,3],[3,2],[5,0],[5,2],[8,2],[8,1],[8,1],[8,1],[7,4],[7,6],[10,1],[10,5],[10,4],[10,3],[10,1],[10,2],[10,2],[10,2],[10,1],[17,1],[17,1],[21,1],[21,2],[32,2],[32,3],[32,3],[32,4],[33,1],[34,1],[35,1],[35,3],[38,1],[36,1],[18,3],[42,1],[42,1],[42,1],[42,1],[43,1],[43,1],[19,1],[19,1],[19,1],[12,1],[13,1],[16,1],[14,1]],performAction:function(t,e,i,r,n,a,s){var o=a.length-1;switch(n){case 1:break;case 3:case 7:case 8:this.$=[];break;case 4:a[o-1].push(a[o]),this.$=a[o-1];break;case 5:case 6:case 20:case 43:case 28:case 29:case 32:this.$=a[o];break;case 12:r.addEntity(a[o-4]),r.addEntity(a[o-2]),r.addRelationship(a[o-4],a[o],a[o-2],a[o-3]);break;case 13:r.addEntity(a[o-3]),r.addAttributes(a[o-3],a[o-1]);break;case 14:r.addEntity(a[o-2]);break;case 15:r.addEntity(a[o]);break;case 16:case 17:this.$=a[o].trim(),r.setAccTitle(this.$);break;case 18:case 19:this.$=a[o].trim(),r.setAccDescription(this.$);break;case 21:case 41:case 42:case 33:this.$=a[o].replace(/"/g,"");break;case 22:case 30:this.$=[a[o]];break;case 23:a[o].push(a[o-1]),this.$=a[o];break;case 24:this.$={attributeType:a[o-1],attributeName:a[o]};break;case 25:this.$={attributeType:a[o-2],attributeName:a[o-1],attributeKeyTypeList:a[o]};break;case 26:this.$={attributeType:a[o-2],attributeName:a[o-1],attributeComment:a[o]};break;case 27:this.$={attributeType:a[o-3],attributeName:a[o-2],attributeKeyTypeList:a[o-1],attributeComment:a[o]};break;case 31:a[o-2].push(a[o]),this.$=a[o-2];break;case 34:this.$={cardA:a[o],relType:a[o-1],cardB:a[o-2]};break;case 35:this.$=r.Cardinality.ZERO_OR_ONE;break;case 36:this.$=r.Cardinality.ZERO_OR_MORE;break;case 37:this.$=r.Cardinality.ONE_OR_MORE;break;case 38:this.$=r.Cardinality.ONLY_ONE;break;case 39:this.$=r.Identification.NON_IDENTIFYING;break;case 40:this.$=r.Identification.IDENTIFYING;break;case 44:r.parseDirective("%%{","open_directive");break;case 45:r.parseDirective(a[o],"type_directive");break;case 46:a[o]=a[o].trim().replace(/'/g,'"'),r.parseDirective(a[o],"arg_directive");break;case 47:r.parseDirective("}%%","close_directive","er")}},table:[{3:1,4:e,7:3,12:4,51:i},{1:[3]},t(r,[2,3],{5:6}),{3:7,4:e,7:3,12:4,51:i},{13:8,52:[1,9]},{52:[2,44]},{6:[1,10],7:15,8:11,9:[1,12],10:13,11:[1,14],12:4,17:16,23:n,25:a,27:s,29:o,30:c,31:l,51:i},{1:[2,2]},{14:23,15:[1,24],54:h},t([15,54],[2,45]),t(r,[2,8],{1:[2,1]}),t(r,[2,4]),{7:15,10:26,12:4,17:16,23:n,25:a,27:s,29:o,30:c,31:l,51:i},t(r,[2,6]),t(r,[2,7]),t(r,[2,11]),t(r,[2,15],{18:27,42:29,20:[1,28],44:d,45:y,46:u,47:p}),{24:[1,34]},{26:[1,35]},{28:[1,36]},t(r,[2,19]),t(_,[2,20]),t(_,[2,21]),{11:[1,37]},{16:38,53:[1,39]},{11:[2,47]},t(r,[2,5]),{17:40,30:c,31:l},{21:41,22:[1,42],32:43,33:44,37:f},{43:46,48:[1,47],49:[1,48]},t(g,[2,35]),t(g,[2,36]),t(g,[2,37]),t(g,[2,38]),t(r,[2,16]),t(r,[2,17]),t(r,[2,18]),t(m,[2,9]),{14:49,54:h},{54:[2,46]},{15:[1,50]},{22:[1,51]},t(r,[2,14]),{21:52,22:[2,22],32:43,33:44,37:f},{34:53,37:[1,54]},{37:[2,28]},{42:55,44:d,45:y,46:u,47:p},t(O,[2,39]),t(O,[2,40]),{11:[1,56]},{19:57,30:[1,60],31:[1,59],50:[1,58]},t(r,[2,13]),{22:[2,23]},t(E,[2,24],{35:61,36:62,38:63,40:b,41:k}),t([22,37,40,41],[2,29]),t([30,31],[2,34]),t(m,[2,10]),t(r,[2,12]),t(r,[2,41]),t(r,[2,42]),t(r,[2,43]),t(E,[2,25],{36:66,39:[1,67],41:k}),t(E,[2,26]),t(R,[2,30]),t(E,[2,33]),t(R,[2,32]),t(E,[2,27]),{38:68,40:b},t(R,[2,31])],defaultActions:{5:[2,44],7:[2,2],25:[2,47],39:[2,46],45:[2,28],52:[2,23]},parseError:function(t,e){if(!e.recoverable){var i=new Error(t);throw i.hash=e,i}this.trace(t)},parse:function(t){var e=this,i=[0],r=[],n=[null],a=[],s=this.table,o="",c=0,l=0,h=2,d=1,y=a.slice.call(arguments,1),u=Object.create(this.lexer),p={yy:{}};for(var _ in this.yy)Object.prototype.hasOwnProperty.call(this.yy,_)&&(p.yy[_]=this.yy[_]);u.setInput(t,p.yy),p.yy.lexer=u,p.yy.parser=this,void 0===u.yylloc&&(u.yylloc={});var f=u.yylloc;a.push(f);var g=u.options&&u.options.ranges;function m(){var t;return"number"!=typeof(t=r.pop()||u.lex()||d)&&(t instanceof Array&&(t=(r=t).pop()),t=e.symbols_[t]||t),t}"function"==typeof p.yy.parseError?this.parseError=p.yy.parseError:this.parseError=Object.getPrototypeOf(this).parseError;for(var O,E,b,k,R,N,x,T,v={};;){if(E=i[i.length-1],this.defaultActions[E]?b=this.defaultActions[E]:(null==O&&(O=m()),b=s[E]&&s[E][O]),void 0===b||!b.length||!b[0]){var A="";for(R in T=[],s[E])this.terminals_[R]&&R>h&&T.push("'"+this.terminals_[R]+"'");A=u.showPosition?"Parse error on line "+(c+1)+":\n"+u.showPosition()+"\nExpecting "+T.join(", ")+", got '"+(this.terminals_[O]||O)+"'":"Parse error on line "+(c+1)+": Unexpected "+(O==d?"end of input":"'"+(this.terminals_[O]||O)+"'"),this.parseError(A,{text:u.match,token:this.terminals_[O]||O,line:u.yylineno,loc:f,expected:T})}if(b[0]instanceof Array&&b.length>1)throw new Error("Parse Error: multiple actions possible at state: "+E+", token: "+O);switch(b[0]){case 1:i.push(O),n.push(u.yytext),a.push(u.yylloc),i.push(b[1]),O=null,l=u.yyleng,o=u.yytext,c=u.yylineno,f=u.yylloc;break;case 2:if(N=this.productions_[b[1]][1],v.$=n[n.length-N],v._$={first_line:a[a.length-(N||1)].first_line,last_line:a[a.length-1].last_line,first_column:a[a.length-(N||1)].first_column,last_column:a[a.length-1].last_column},g&&(v._$.range=[a[a.length-(N||1)].range[0],a[a.length-1].range[1]]),void 0!==(k=this.performAction.apply(v,[o,l,c,p.yy,b[1],n,a].concat(y))))return k;N&&(i=i.slice(0,-1*N*2),n=n.slice(0,-1*N),a=a.slice(0,-1*N)),i.push(this.productions_[b[1]][0]),n.push(v.$),a.push(v._$),x=s[i[i.length-2]][i[i.length-1]],i.push(x);break;case 3:return!0}}return!0}},x={EOF:1,parseError:function(t,e){if(!this.yy.parser)throw new Error(t);this.yy.parser.parseError(t,e)},setInput:function(t,e){return this.yy=e||this.yy||{},this._input=t,this._more=this._backtrack=this.done=!1,this.yylineno=this.yyleng=0,this.yytext=this.matched=this.match="",this.conditionStack=["INITIAL"],this.yylloc={first_line:1,first_column:0,last_line:1,last_column:0},this.options.ranges&&(this.yylloc.range=[0,0]),this.offset=0,this},input:function(){var t=this._input[0];return this.yytext+=t,this.yyleng++,this.offset++,this.match+=t,this.matched+=t,t.match(/(?:\r\n?|\n).*/g)?(this.yylineno++,this.yylloc.last_line++):this.yylloc.last_column++,this.options.ranges&&this.yylloc.range[1]++,this._input=this._input.slice(1),t},unput:function(t){var e=t.length,i=t.split(/(?:\r\n?|\n)/g);this._input=t+this._input,this.yytext=this.yytext.substr(0,this.yytext.length-e),this.offset-=e;var r=this.match.split(/(?:\r\n?|\n)/g);this.match=this.match.substr(0,this.match.length-1),this.matched=this.matched.substr(0,this.matched.length-1),i.length-1&&(this.yylineno-=i.length-1);var n=this.yylloc.range;return this.yylloc={first_line:this.yylloc.first_line,last_line:this.yylineno+1,first_column:this.yylloc.first_column,last_column:i?(i.length===r.length?this.yylloc.first_column:0)+r[r.length-i.length].length-i[0].length:this.yylloc.first_column-e},this.options.ranges&&(this.yylloc.range=[n[0],n[0]+this.yyleng-e]),this.yyleng=this.yytext.length,this},more:function(){return this._more=!0,this},reject:function(){return this.options.backtrack_lexer?(this._backtrack=!0,this):this.parseError("Lexical error on line "+(this.yylineno+1)+". You can only invoke reject() in the lexer when the lexer is of the backtracking persuasion (options.backtrack_lexer = true).\n"+this.showPosition(),{text:"",token:null,line:this.yylineno})},less:function(t){this.unput(this.match.slice(t))},pastInput:function(){var t=this.matched.substr(0,this.matched.length-this.match.length);return(t.length>20?"...":"")+t.substr(-20).replace(/\n/g,"")},upcomingInput:function(){var t=this.match;return t.length<20&&(t+=this._input.substr(0,20-t.length)),(t.substr(0,20)+(t.length>20?"...":"")).replace(/\n/g,"")},showPosition:function(){var t=this.pastInput(),e=new Array(t.length+1).join("-");return t+this.upcomingInput()+"\n"+e+"^"},test_match:function(t,e){var i,r,n;if(this.options.backtrack_lexer&&(n={yylineno:this.yylineno,yylloc:{first_line:this.yylloc.first_line,last_line:this.last_line,first_column:this.yylloc.first_column,last_column:this.yylloc.last_column},yytext:this.yytext,match:this.match,matches:this.matches,matched:this.matched,yyleng:this.yyleng,offset:this.offset,_more:this._more,_input:this._input,yy:this.yy,conditionStack:this.conditionStack.slice(0),done:this.done},this.options.ranges&&(n.yylloc.range=this.yylloc.range.slice(0))),(r=t[0].match(/(?:\r\n?|\n).*/g))&&(this.yylineno+=r.length),this.yylloc={first_line:this.yylloc.last_line,last_line:this.yylineno+1,first_column:this.yylloc.last_column,last_column:r?r[r.length-1].length-r[r.length-1].match(/\r?\n?/)[0].length:this.yylloc.last_column+t[0].length},this.yytext+=t[0],this.match+=t[0],this.matches=t,this.yyleng=this.yytext.length,this.options.ranges&&(this.yylloc.range=[this.offset,this.offset+=this.yyleng]),this._more=!1,this._backtrack=!1,this._input=this._input.slice(t[0].length),this.matched+=t[0],i=this.performAction.call(this,this.yy,this,e,this.conditionStack[this.conditionStack.length-1]),this.done&&this._input&&(this.done=!1),i)return i;if(this._backtrack){for(var a in n)this[a]=n[a];return!1}return!1},next:function(){if(this.done)return this.EOF;var t,e,i,r;this._input||(this.done=!0),this._more||(this.yytext="",this.match="");for(var n=this._currentRules(),a=0;a<n.length;a++)if((i=this._input.match(this.rules[n[a]]))&&(!e||i[0].length>e[0].length)){if(e=i,r=a,this.options.backtrack_lexer){if(!1!==(t=this.test_match(i,n[a])))return t;if(this._backtrack){e=!1;continue}return!1}if(!this.options.flex)break}return e?!1!==(t=this.test_match(e,n[r]))&&t:""===this._input?this.EOF:this.parseError("Lexical error on line "+(this.yylineno+1)+". Unrecognized text.\n"+this.showPosition(),{text:"",token:null,line:this.yylineno})},lex:function(){var t=this.next();return t||this.lex()},begin:function(t){this.conditionStack.push(t)},popState:function(){return this.conditionStack.length-1>0?this.conditionStack.pop():this.conditionStack[0]},_currentRules:function(){return this.conditionStack.length&&this.conditionStack[this.conditionStack.length-1]?this.conditions[this.conditionStack[this.conditionStack.length-1]].rules:this.conditions.INITIAL.rules},topState:function(t){return(t=this.conditionStack.length-1-Math.abs(t||0))>=0?this.conditionStack[t]:"INITIAL"},pushState:function(t){this.begin(t)},stateStackSize:function(){return this.conditionStack.length},options:{"case-insensitive":!0},performAction:function(t,e,i,r){switch(i){case 0:return this.begin("acc_title"),25;case 1:return this.popState(),"acc_title_value";case 2:return this.begin("acc_descr"),27;case 3:return this.popState(),"acc_descr_value";case 4:this.begin("acc_descr_multiline");break;case 5:this.popState();break;case 6:return"acc_descr_multiline_value";case 7:return this.begin("open_directive"),51;case 8:return this.begin("type_directive"),52;case 9:return this.popState(),this.begin("arg_directive"),15;case 10:return this.popState(),this.popState(),54;case 11:return 53;case 12:return 11;case 13:case 20:case 25:break;case 14:return 9;case 15:return 31;case 16:return 50;case 17:return 4;case 18:return this.begin("block"),20;case 19:return 39;case 21:return 40;case 22:case 23:return 37;case 24:return 41;case 26:return this.popState(),22;case 27:case 56:return e.yytext[0];case 28:case 32:case 33:case 46:return 44;case 29:case 30:case 31:case 39:case 41:case 48:return 46;case 34:case 35:case 36:case 37:case 38:case 40:case 47:return 45;case 42:case 43:case 44:case 45:return 47;case 49:case 52:case 53:case 54:return 48;case 50:case 51:return 49;case 55:return 30;case 57:return 6}},rules:[/^(?:accTitle\s*:\s*)/i,/^(?:(?!\n||)*[^\n]*)/i,/^(?:accDescr\s*:\s*)/i,/^(?:(?!\n||)*[^\n]*)/i,/^(?:accDescr\s*\{\s*)/i,/^(?:[\}])/i,/^(?:[^\}]*)/i,/^(?:%%\{)/i,/^(?:((?:(?!\}%%)[^:.])*))/i,/^(?::)/i,/^(?:\}%%)/i,/^(?:((?:(?!\}%%).|\n)*))/i,/^(?:[\n]+)/i,/^(?:\s+)/i,/^(?:[\s]+)/i,/^(?:"[^"%\r\n\v\b\\]+")/i,/^(?:"[^"]*")/i,/^(?:erDiagram\b)/i,/^(?:\{)/i,/^(?:,)/i,/^(?:\s+)/i,/^(?:\b((?:PK)|(?:FK)|(?:UK))\b)/i,/^(?:(.*?)[~](.*?)*[~])/i,/^(?:[A-Za-z_][A-Za-z0-9\-_\[\]\(\)]*)/i,/^(?:"[^"]*")/i,/^(?:[\n]+)/i,/^(?:\})/i,/^(?:.)/i,/^(?:one or zero\b)/i,/^(?:one or more\b)/i,/^(?:one or many\b)/i,/^(?:1\+)/i,/^(?:\|o\b)/i,/^(?:zero or one\b)/i,/^(?:zero or more\b)/i,/^(?:zero or many\b)/i,/^(?:0\+)/i,/^(?:\}o\b)/i,/^(?:many\(0\))/i,/^(?:many\(1\))/i,/^(?:many\b)/i,/^(?:\}\|)/i,/^(?:one\b)/i,/^(?:only one\b)/i,/^(?:1\b)/i,/^(?:\|\|)/i,/^(?:o\|)/i,/^(?:o\{)/i,/^(?:\|\{)/i,/^(?:\.\.)/i,/^(?:--)/i,/^(?:to\b)/i,/^(?:optionally to\b)/i,/^(?:\.-)/i,/^(?:-\.)/i,/^(?:[A-Za-z][A-Za-z0-9\-_]*)/i,/^(?:.)/i,/^(?:$)/i],conditions:{acc_descr_multiline:{rules:[5,6],inclusive:!1},acc_descr:{rules:[3],inclusive:!1},acc_title:{rules:[1],inclusive:!1},open_directive:{rules:[8],inclusive:!1},type_directive:{rules:[9,10],inclusive:!1},arg_directive:{rules:[10,11],inclusive:!1},block:{rules:[19,20,21,22,23,24,25,26,27],inclusive:!1},INITIAL:{rules:[0,2,4,7,12,13,14,15,16,17,18,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57],inclusive:!0}}};function T(){this.yy={}}return N.lexer=x,T.prototype=N,N.Parser=T,new T}();g.parser=g;const m=g;let O={},E=[];const b=function(t){return void 0===O[t]&&(O[t]={attributes:[]},r.l.info("Added new entity :",t)),O[t]},k={Cardinality:{ZERO_OR_ONE:"ZERO_OR_ONE",ZERO_OR_MORE:"ZERO_OR_MORE",ONE_OR_MORE:"ONE_OR_MORE",ONLY_ONE:"ONLY_ONE"},Identification:{NON_IDENTIFYING:"NON_IDENTIFYING",IDENTIFYING:"IDENTIFYING"},parseDirective:function(t,e,i){n.m.parseDirective(this,t,e,i)},getConfig:()=>(0,r.g)().er,addEntity:b,addAttributes:function(t,e){let i,n=b(t);for(i=e.length-1;i>=0;i--)n.attributes.push(e[i]),r.l.debug("Added attribute ",e[i].attributeName)},getEntities:()=>O,addRelationship:function(t,e,i,n){let a={entityA:t,roleA:e,entityB:i,relSpec:n};E.push(a),r.l.debug("Added new relationship :",a)},getRelationships:()=>E,clear:function(){O={},E=[],(0,r.y)()},setAccTitle:r.o,getAccTitle:r.p,setAccDescription:r.v,getAccDescription:r.q,setDiagramTitle:r.w,getDiagramTitle:r.x},R={ONLY_ONE_START:"ONLY_ONE_START",ONLY_ONE_END:"ONLY_ONE_END",ZERO_OR_ONE_START:"ZERO_OR_ONE_START",ZERO_OR_ONE_END:"ZERO_OR_ONE_END",ONE_OR_MORE_START:"ONE_OR_MORE_START",ONE_OR_MORE_END:"ONE_OR_MORE_END",ZERO_OR_MORE_START:"ZERO_OR_MORE_START",ZERO_OR_MORE_END:"ZERO_OR_MORE_END"},N=R,x=function(t,e){let i;t.append("defs").append("marker").attr("id",R.ONLY_ONE_START).attr("refX",0).attr("refY",9).attr("markerWidth",18).attr("markerHeight",18).attr("orient","auto").append("path").attr("stroke",e.stroke).attr("fill","none").attr("d","M9,0 L9,18 M15,0 L15,18"),t.append("defs").append("marker").attr("id",R.ONLY_ONE_END).attr("refX",18).attr("refY",9).attr("markerWidth",18).attr("markerHeight",18).attr("orient","auto").append("path").attr("stroke",e.stroke).attr("fill","none").attr("d","M3,0 L3,18 M9,0 L9,18"),i=t.append("defs").append("marker").attr("id",R.ZERO_OR_ONE_START).attr("refX",0).attr("refY",9).attr("markerWidth",30).attr("markerHeight",18).attr("orient","auto"),i.append("circle").attr("stroke",e.stroke).attr("fill","white").attr("cx",21).attr("cy",9).attr("r",6),i.append("path").attr("stroke",e.stroke).attr("fill","none").attr("d","M9,0 L9,18"),i=t.append("defs").append("marker").attr("id",R.ZERO_OR_ONE_END).attr("refX",30).attr("refY",9).attr("markerWidth",30).attr("markerHeight",18).attr("orient","auto"),i.append("circle").attr("stroke",e.stroke).attr("fill","white").attr("cx",9).attr("cy",9).attr("r",6),i.append("path").attr("stroke",e.stroke).attr("fill","none").attr("d","M21,0 L21,18"),t.append("defs").append("marker").attr("id",R.ONE_OR_MORE_START).attr("refX",18).attr("refY",18).attr("markerWidth",45).attr("markerHeight",36).attr("orient","auto").append("path").attr("stroke",e.stroke).attr("fill","none").attr("d","M0,18 Q 18,0 36,18 Q 18,36 0,18 M42,9 L42,27"),t.append("defs").append("marker").attr("id",R.ONE_OR_MORE_END).attr("refX",27).attr("refY",18).attr("markerWidth",45).attr("markerHeight",36).attr("orient","auto").append("path").attr("stroke",e.stroke).attr("fill","none").attr("d","M3,9 L3,27 M9,18 Q27,0 45,18 Q27,36 9,18"),i=t.append("defs").append("marker").attr("id",R.ZERO_OR_MORE_START).attr("refX",18).attr("refY",18).attr("markerWidth",57).attr("markerHeight",36).attr("orient","auto"),i.append("circle").attr("stroke",e.stroke).attr("fill","white").attr("cx",48).attr("cy",18).attr("r",6),i.append("path").attr("stroke",e.stroke).attr("fill","none").attr("d","M0,18 Q18,0 36,18 Q18,36 0,18"),i=t.append("defs").append("marker").attr("id",R.ZERO_OR_MORE_END).attr("refX",39).attr("refY",18).attr("markerWidth",57).attr("markerHeight",36).attr("orient","auto"),i.append("circle").attr("stroke",e.stroke).attr("fill","white").attr("cx",9).attr("cy",18).attr("r",6),i.append("path").attr("stroke",e.stroke).attr("fill","none").attr("d","M21,18 Q39,0 57,18 Q39,36 21,18")},T=/[^\dA-Za-z](\W)*/g;let v={},A=new Map;const w=function(t,e,i){let n;return Object.keys(e).forEach((function(a){const s=function(t="",e=""){const i=t.replace(T,"");return`${$(e)}${$(i)}${f(t,S)}`}(a,"entity");A.set(a,s);const o=t.append("g").attr("id",s);n=void 0===n?s:n;const c="text-"+s,l=o.append("text").classed("er entityLabel",!0).attr("id",c).attr("x",0).attr("y",0).style("dominant-baseline","middle").style("text-anchor","middle").style("font-family",(0,r.g)().fontFamily).style("font-size",v.fontSize+"px").text(a),{width:h,height:d}=((t,e,i)=>{const n=v.entityPadding/3,a=v.entityPadding/3,s=.85*v.fontSize,o=e.node().getBBox(),c=[];let l=!1,h=!1,d=0,y=0,u=0,p=0,_=o.height+2*n,f=1;i.forEach((t=>{void 0!==t.attributeKeyTypeList&&t.attributeKeyTypeList.length>0&&(l=!0),void 0!==t.attributeComment&&(h=!0)})),i.forEach((i=>{const a=`${e.node().id}-attr-${f}`;let o=0;const g=(0,r.z)(i.attributeType),m=t.append("text").classed("er entityLabel",!0).attr("id",`${a}-type`).attr("x",0).attr("y",0).style("dominant-baseline","middle").style("text-anchor","left").style("font-family",(0,r.g)().fontFamily).style("font-size",s+"px").text(g),O=t.append("text").classed("er entityLabel",!0).attr("id",`${a}-name`).attr("x",0).attr("y",0).style("dominant-baseline","middle").style("text-anchor","left").style("font-family",(0,r.g)().fontFamily).style("font-size",s+"px").text(i.attributeName),E={};E.tn=m,E.nn=O;const b=m.node().getBBox(),k=O.node().getBBox();if(d=Math.max(d,b.width),y=Math.max(y,k.width),o=Math.max(b.height,k.height),l){const e=void 0!==i.attributeKeyTypeList?i.attributeKeyTypeList.join(","):"",n=t.append("text").classed("er entityLabel",!0).attr("id",`${a}-key`).attr("x",0).attr("y",0).style("dominant-baseline","middle").style("text-anchor","left").style("font-family",(0,r.g)().fontFamily).style("font-size",s+"px").text(e);E.kn=n;const c=n.node().getBBox();u=Math.max(u,c.width),o=Math.max(o,c.height)}if(h){const e=t.append("text").classed("er entityLabel",!0).attr("id",`${a}-comment`).attr("x",0).attr("y",0).style("dominant-baseline","middle").style("text-anchor","left").style("font-family",(0,r.g)().fontFamily).style("font-size",s+"px").text(i.attributeComment||"");E.cn=e;const n=e.node().getBBox();p=Math.max(p,n.width),o=Math.max(o,n.height)}E.height=o,c.push(E),_+=o+2*n,f+=1}));let g=4;l&&(g+=2),h&&(g+=2);const m=d+y+u+p,O={width:Math.max(v.minEntityWidth,Math.max(o.width+2*v.entityPadding,m+a*g)),height:i.length>0?_:Math.max(v.minEntityHeight,o.height+2*v.entityPadding)};if(i.length>0){const i=Math.max(0,(O.width-m-a*g)/(g/2));e.attr("transform","translate("+O.width/2+","+(n+o.height/2)+")");let r=o.height+2*n,s="attributeBoxOdd";c.forEach((e=>{const o=r+n+e.height/2;e.tn.attr("transform","translate("+a+","+o+")");const c=t.insert("rect","#"+e.tn.node().id).classed(`er ${s}`,!0).attr("x",0).attr("y",r).attr("width",d+2*a+i).attr("height",e.height+2*n),_=parseFloat(c.attr("x"))+parseFloat(c.attr("width"));e.nn.attr("transform","translate("+(_+a)+","+o+")");const f=t.insert("rect","#"+e.nn.node().id).classed(`er ${s}`,!0).attr("x",_).attr("y",r).attr("width",y+2*a+i).attr("height",e.height+2*n);let g=parseFloat(f.attr("x"))+parseFloat(f.attr("width"));if(l){e.kn.attr("transform","translate("+(g+a)+","+o+")");const c=t.insert("rect","#"+e.kn.node().id).classed(`er ${s}`,!0).attr("x",g).attr("y",r).attr("width",u+2*a+i).attr("height",e.height+2*n);g=parseFloat(c.attr("x"))+parseFloat(c.attr("width"))}h&&(e.cn.attr("transform","translate("+(g+a)+","+o+")"),t.insert("rect","#"+e.cn.node().id).classed(`er ${s}`,"true").attr("x",g).attr("y",r).attr("width",p+2*a+i).attr("height",e.height+2*n)),r+=e.height+2*n,s="attributeBoxOdd"===s?"attributeBoxEven":"attributeBoxOdd"}))}else O.height=Math.max(v.minEntityHeight,_),e.attr("transform","translate("+O.width/2+","+O.height/2+")");return O})(o,l,e[a].attributes),y=o.insert("rect","#"+c).classed("er entityBox",!0).attr("x",0).attr("y",0).attr("width",h).attr("height",d).node().getBBox();i.setNode(s,{width:y.width,height:y.height,shape:"rect",id:s})})),n},M=function(t){return(t.entityA+t.roleA+t.entityB).replace(/\s/g,"")};let I=0;const S="28e9f9db-3c8d-5aa5-9faf-44286ae5937c";function $(t=""){return t.length>0?`${t}-`:""}const L={parser:m,db:k,renderer:{setConf:function(t){const e=Object.keys(t);for(const i of e)v[i]=t[i]},draw:function(t,e,i,n){v=(0,r.g)().er,r.l.info("Drawing ER diagram");const l=(0,r.g)().securityLevel;let h;"sandbox"===l&&(h=(0,s.Ys)("#i"+e));const d=("sandbox"===l?(0,s.Ys)(h.nodes()[0].contentDocument.body):(0,s.Ys)("body")).select(`[id='${e}']`);let y;x(d,v),y=new a.k({multigraph:!0,directed:!0,compound:!1}).setGraph({rankdir:v.layoutDirection,marginx:20,marginy:20,nodesep:100,edgesep:100,ranksep:100}).setDefaultEdgeLabel((function(){return{}}));const u=w(d,n.db.getEntities(),y),p=function(t,e){return t.forEach((function(t){e.setEdge(A.get(t.entityA),A.get(t.entityB),{relationship:t},M(t))})),t}(n.db.getRelationships(),y);var _,f;(0,o.bK)(y),_=d,(f=y).nodes().forEach((function(t){void 0!==t&&void 0!==f.node(t)&&_.select("#"+t).attr("transform","translate("+(f.node(t).x-f.node(t).width/2)+","+(f.node(t).y-f.node(t).height/2)+" )")})),p.forEach((function(t){!function(t,e,i,n,a){I++;const o=i.edge(A.get(e.entityA),A.get(e.entityB),M(e)),c=(0,s.jvg)().x((function(t){return t.x})).y((function(t){return t.y})).curve(s.$0Z),l=t.insert("path","#"+n).classed("er relationshipLine",!0).attr("d",c(o.points)).style("stroke",v.stroke).style("fill","none");e.relSpec.relType===a.db.Identification.NON_IDENTIFYING&&l.attr("stroke-dasharray","8,8");let h="";switch(v.arrowMarkerAbsolute&&(h=window.location.protocol+"//"+window.location.host+window.location.pathname+window.location.search,h=h.replace(/\(/g,"\\("),h=h.replace(/\)/g,"\\)")),e.relSpec.cardA){case a.db.Cardinality.ZERO_OR_ONE:l.attr("marker-end","url("+h+"#"+N.ZERO_OR_ONE_END+")");break;case a.db.Cardinality.ZERO_OR_MORE:l.attr("marker-end","url("+h+"#"+N.ZERO_OR_MORE_END+")");break;case a.db.Cardinality.ONE_OR_MORE:l.attr("marker-end","url("+h+"#"+N.ONE_OR_MORE_END+")");break;case a.db.Cardinality.ONLY_ONE:l.attr("marker-end","url("+h+"#"+N.ONLY_ONE_END+")")}switch(e.relSpec.cardB){case a.db.Cardinality.ZERO_OR_ONE:l.attr("marker-start","url("+h+"#"+N.ZERO_OR_ONE_START+")");break;case a.db.Cardinality.ZERO_OR_MORE:l.attr("marker-start","url("+h+"#"+N.ZERO_OR_MORE_START+")");break;case a.db.Cardinality.ONE_OR_MORE:l.attr("marker-start","url("+h+"#"+N.ONE_OR_MORE_START+")");break;case a.db.Cardinality.ONLY_ONE:l.attr("marker-start","url("+h+"#"+N.ONLY_ONE_START+")")}const d=l.node().getTotalLength(),y=l.node().getPointAtLength(.5*d),u="rel"+I,p=t.append("text").classed("er relationshipLabel",!0).attr("id",u).attr("x",y.x).attr("y",y.y).style("text-anchor","middle").style("dominant-baseline","middle").style("font-family",(0,r.g)().fontFamily).style("font-size",v.fontSize+"px").text(e.roleA).node().getBBox();t.insert("rect","#"+u).classed("er relationshipLabelBox",!0).attr("x",y.x-p.width/2).attr("y",y.y-p.height/2).attr("width",p.width).attr("height",p.height)}(d,t,y,u,n)}));const g=v.diagramPadding;c.u.insertTitle(d,"entityTitleText",v.titleTopMargin,n.db.getDiagramTitle());const m=d.node().getBBox(),O=m.width+2*g,E=m.height+2*g;(0,c.k)(d,E,O,v.useMaxWidth),d.attr("viewBox",`${m.x-g} ${m.y-g} ${O} ${E}`)}},styles:t=>`\n  .entityBox {\n    fill: ${t.mainBkg};\n    stroke: ${t.nodeBorder};\n  }\n\n  .attributeBoxOdd {\n    fill: ${t.attributeBackgroundColorOdd};\n    stroke: ${t.nodeBorder};\n  }\n\n  .attributeBoxEven {\n    fill:  ${t.attributeBackgroundColorEven};\n    stroke: ${t.nodeBorder};\n  }\n\n  .relationshipLabelBox {\n    fill: ${t.tertiaryColor};\n    opacity: 0.7;\n    background-color: ${t.tertiaryColor};\n      rect {\n        opacity: 0.5;\n      }\n  }\n\n    .relationshipLine {\n      stroke: ${t.lineColor};\n    }\n\n  .entityTitleText {\n    text-anchor: middle;\n    font-size: 18px;\n    fill: ${t.textColor};\n  }    \n`}}}]);