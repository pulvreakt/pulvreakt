"use strict";(self.webpackChunkdocsite=self.webpackChunkdocsite||[]).push([[8099],{8099:(t,e,r)=>{r.d(e,{a:()=>c,b:()=>B,c:()=>h,d:()=>N,e:()=>M,f:()=>P,g:()=>I,h:()=>H,i:()=>b,l:()=>p,p:()=>E,s:()=>T,u:()=>g});var a=r(44),i=r(4646),n=r(211),s=r(3047),l=r(7657),d=r(1188);const o={extension:(t,e,r)=>{a.l.trace("Making markers for ",r),t.append("defs").append("marker").attr("id",e+"-extensionStart").attr("class","marker extension "+e).attr("refX",0).attr("refY",7).attr("markerWidth",190).attr("markerHeight",240).attr("orient","auto").append("path").attr("d","M 1,7 L18,13 V 1 Z"),t.append("defs").append("marker").attr("id",e+"-extensionEnd").attr("class","marker extension "+e).attr("refX",19).attr("refY",7).attr("markerWidth",20).attr("markerHeight",28).attr("orient","auto").append("path").attr("d","M 1,1 V 13 L18,7 Z")},composition:(t,e)=>{t.append("defs").append("marker").attr("id",e+"-compositionStart").attr("class","marker composition "+e).attr("refX",0).attr("refY",7).attr("markerWidth",190).attr("markerHeight",240).attr("orient","auto").append("path").attr("d","M 18,7 L9,13 L1,7 L9,1 Z"),t.append("defs").append("marker").attr("id",e+"-compositionEnd").attr("class","marker composition "+e).attr("refX",19).attr("refY",7).attr("markerWidth",20).attr("markerHeight",28).attr("orient","auto").append("path").attr("d","M 18,7 L9,13 L1,7 L9,1 Z")},aggregation:(t,e)=>{t.append("defs").append("marker").attr("id",e+"-aggregationStart").attr("class","marker aggregation "+e).attr("refX",0).attr("refY",7).attr("markerWidth",190).attr("markerHeight",240).attr("orient","auto").append("path").attr("d","M 18,7 L9,13 L1,7 L9,1 Z"),t.append("defs").append("marker").attr("id",e+"-aggregationEnd").attr("class","marker aggregation "+e).attr("refX",19).attr("refY",7).attr("markerWidth",20).attr("markerHeight",28).attr("orient","auto").append("path").attr("d","M 18,7 L9,13 L1,7 L9,1 Z")},dependency:(t,e)=>{t.append("defs").append("marker").attr("id",e+"-dependencyStart").attr("class","marker dependency "+e).attr("refX",0).attr("refY",7).attr("markerWidth",190).attr("markerHeight",240).attr("orient","auto").append("path").attr("d","M 5,7 L9,13 L1,7 L9,1 Z"),t.append("defs").append("marker").attr("id",e+"-dependencyEnd").attr("class","marker dependency "+e).attr("refX",19).attr("refY",7).attr("markerWidth",20).attr("markerHeight",28).attr("orient","auto").append("path").attr("d","M 18,7 L9,13 L14,7 L9,1 Z")},lollipop:(t,e)=>{t.append("defs").append("marker").attr("id",e+"-lollipopStart").attr("class","marker lollipop "+e).attr("refX",0).attr("refY",7).attr("markerWidth",190).attr("markerHeight",240).attr("orient","auto").append("circle").attr("stroke","black").attr("fill","white").attr("cx",6).attr("cy",7).attr("r",6)},point:(t,e)=>{t.append("marker").attr("id",e+"-pointEnd").attr("class","marker "+e).attr("viewBox","0 0 12 20").attr("refX",10).attr("refY",5).attr("markerUnits","userSpaceOnUse").attr("markerWidth",12).attr("markerHeight",12).attr("orient","auto").append("path").attr("d","M 0 0 L 10 5 L 0 10 z").attr("class","arrowMarkerPath").style("stroke-width",1).style("stroke-dasharray","1,0"),t.append("marker").attr("id",e+"-pointStart").attr("class","marker "+e).attr("viewBox","0 0 10 10").attr("refX",0).attr("refY",5).attr("markerUnits","userSpaceOnUse").attr("markerWidth",12).attr("markerHeight",12).attr("orient","auto").append("path").attr("d","M 0 5 L 10 10 L 10 0 z").attr("class","arrowMarkerPath").style("stroke-width",1).style("stroke-dasharray","1,0")},circle:(t,e)=>{t.append("marker").attr("id",e+"-circleEnd").attr("class","marker "+e).attr("viewBox","0 0 10 10").attr("refX",11).attr("refY",5).attr("markerUnits","userSpaceOnUse").attr("markerWidth",11).attr("markerHeight",11).attr("orient","auto").append("circle").attr("cx","5").attr("cy","5").attr("r","5").attr("class","arrowMarkerPath").style("stroke-width",1).style("stroke-dasharray","1,0"),t.append("marker").attr("id",e+"-circleStart").attr("class","marker "+e).attr("viewBox","0 0 10 10").attr("refX",-1).attr("refY",5).attr("markerUnits","userSpaceOnUse").attr("markerWidth",11).attr("markerHeight",11).attr("orient","auto").append("circle").attr("cx","5").attr("cy","5").attr("r","5").attr("class","arrowMarkerPath").style("stroke-width",1).style("stroke-dasharray","1,0")},cross:(t,e)=>{t.append("marker").attr("id",e+"-crossEnd").attr("class","marker cross "+e).attr("viewBox","0 0 11 11").attr("refX",12).attr("refY",5.2).attr("markerUnits","userSpaceOnUse").attr("markerWidth",11).attr("markerHeight",11).attr("orient","auto").append("path").attr("d","M 1,1 l 9,9 M 10,1 l -9,9").attr("class","arrowMarkerPath").style("stroke-width",2).style("stroke-dasharray","1,0"),t.append("marker").attr("id",e+"-crossStart").attr("class","marker cross "+e).attr("viewBox","0 0 11 11").attr("refX",-1).attr("refY",5.2).attr("markerUnits","userSpaceOnUse").attr("markerWidth",11).attr("markerHeight",11).attr("orient","auto").append("path").attr("d","M 1,1 l 9,9 M 10,1 l -9,9").attr("class","arrowMarkerPath").style("stroke-width",2).style("stroke-dasharray","1,0")},barb:(t,e)=>{t.append("defs").append("marker").attr("id",e+"-barbEnd").attr("refX",19).attr("refY",7).attr("markerWidth",20).attr("markerHeight",14).attr("markerUnits","strokeWidth").attr("orient","auto").append("path").attr("d","M 19,7 L9,13 L14,7 L9,1 Z")}},c=(t,e,r,a)=>{e.forEach((e=>{o[e](t,r,a)}))};const h=(t,e,r,n)=>{let l=t||"";if("object"==typeof l&&(l=l[0]),(0,a.k)((0,a.g)().flowchart.htmlLabels)){l=l.replace(/\\n|\n/g,"<br />"),a.l.info("vertexText"+l);let t=function(t){const e=(0,i.Ys)(document.createElementNS("http://www.w3.org/2000/svg","foreignObject")),r=e.append("xhtml:div"),a=t.label,n=t.isNode?"nodeLabel":"edgeLabel";var s,l;return r.html('<span class="'+n+'" '+(t.labelStyle?'style="'+t.labelStyle+'"':"")+">"+a+"</span>"),s=r,(l=t.labelStyle)&&s.attr("style",l),r.style("display","inline-block"),r.style("white-space","nowrap"),r.attr("xmlns","http://www.w3.org/1999/xhtml"),e.node()}({isNode:n,label:(0,s.d)(l).replace(/fa[blrs]?:fa-[\w-]+/g,(t=>`<i class='${t.replace(":"," ")}'></i>`)),labelStyle:e.replace("fill:","color:")});return t}{const t=document.createElementNS("http://www.w3.org/2000/svg","text");t.setAttribute("style",e.replace("color:","fill:"));let a=[];a="string"==typeof l?l.split(/\\n|\n|<br\s*\/?>/gi):Array.isArray(l)?l:[];for(const e of a){const a=document.createElementNS("http://www.w3.org/2000/svg","tspan");a.setAttributeNS("http://www.w3.org/XML/1998/namespace","xml:space","preserve"),a.setAttribute("dy","1em"),a.setAttribute("x","0"),r?a.setAttribute("class","title-row"):a.setAttribute("class","row"),a.textContent=e.trim(),t.appendChild(a)}return t}},p=(t,e,r,l)=>{let d;const o=e.useHtmlLabels||(0,a.k)((0,a.g)().flowchart.htmlLabels);d=r||"node default";const c=t.insert("g").attr("class",d).attr("id",e.domId||e.id),p=c.insert("g").attr("class","label").attr("style",e.labelStyle);let g;g=void 0===e.labelText?"":"string"==typeof e.labelText?e.labelText:e.labelText[0];const y=p.node();let f;f="markdown"===e.labelType?(0,n.c)(p,(0,a.b)((0,s.d)(g),(0,a.g)()),{useHtmlLabels:o,width:e.width||(0,a.g)().flowchart.wrappingWidth,classes:"markdown-node-label"}):y.appendChild(h((0,a.b)((0,s.d)(g),(0,a.g)()),e.labelStyle,!1,l));let x=f.getBBox();if((0,a.k)((0,a.g)().flowchart.htmlLabels)){const t=f.children[0],e=(0,i.Ys)(f);x=t.getBoundingClientRect(),e.attr("width",x.width),e.attr("height",x.height)}const u=e.padding/2;return o?p.attr("transform","translate("+-x.width/2+", "+-x.height/2+")"):p.attr("transform","translate(0, "+-x.height/2+")"),e.centerLabel&&p.attr("transform","translate("+-x.width/2+", "+-x.height/2+")"),p.insert("rect",":first-child"),{shapeSvg:c,bbox:x,halfPadding:u,label:p}},g=(t,e)=>{const r=e.node().getBBox();t.width=r.width,t.height=r.height};function y(t,e,r,a){return t.insert("polygon",":first-child").attr("points",a.map((function(t){return t.x+","+t.y})).join(" ")).attr("class","label-container").attr("transform","translate("+-e/2+","+r/2+")")}function f(t,e,r,a){var i=t.x,n=t.y,s=i-a.x,l=n-a.y,d=Math.sqrt(e*e*l*l+r*r*s*s),o=Math.abs(e*r*s/d);a.x<i&&(o=-o);var c=Math.abs(e*r*l/d);return a.y<n&&(c=-c),{x:i+o,y:n+c}}function x(t,e,r,a){var i,n,s,l,d,o,c,h,p,g,y,f,x;if(i=e.y-t.y,s=t.x-e.x,d=e.x*t.y-t.x*e.y,p=i*r.x+s*r.y+d,g=i*a.x+s*a.y+d,!(0!==p&&0!==g&&u(p,g)||(n=a.y-r.y,l=r.x-a.x,o=a.x*r.y-r.x*a.y,c=n*t.x+l*t.y+o,h=n*e.x+l*e.y+o,0!==c&&0!==h&&u(c,h)||0==(y=i*l-n*s))))return f=Math.abs(y/2),{x:(x=s*o-l*d)<0?(x-f)/y:(x+f)/y,y:(x=n*d-i*o)<0?(x-f)/y:(x+f)/y}}function u(t,e){return t*e>0}const b=(t,e)=>{var r,a,i=t.x,n=t.y,s=e.x-i,l=e.y-n,d=t.width/2,o=t.height/2;return Math.abs(l)*d>Math.abs(s)*o?(l<0&&(o=-o),r=0===l?0:o*s/l,a=o):(s<0&&(d=-d),r=d,a=0===s?0:d*l/s),{x:i+r,y:n+a}},w={node:function(t,e){return t.intersect(e)},circle:function(t,e,r){return f(t,e,e,r)},ellipse:f,polygon:function(t,e,r){var a=t.x,i=t.y,n=[],s=Number.POSITIVE_INFINITY,l=Number.POSITIVE_INFINITY;"function"==typeof e.forEach?e.forEach((function(t){s=Math.min(s,t.x),l=Math.min(l,t.y)})):(s=Math.min(s,e.x),l=Math.min(l,e.y));for(var d=a-t.width/2-s,o=i-t.height/2-l,c=0;c<e.length;c++){var h=e[c],p=e[c<e.length-1?c+1:0],g=x(t,r,{x:d+h.x,y:o+h.y},{x:d+p.x,y:o+p.y});g&&n.push(g)}return n.length?(n.length>1&&n.sort((function(t,e){var a=t.x-r.x,i=t.y-r.y,n=Math.sqrt(a*a+i*i),s=e.x-r.x,l=e.y-r.y,d=Math.sqrt(s*s+l*l);return n<d?-1:n===d?0:1})),n[0]):t},rect:b},m=(t,e)=>{const{shapeSvg:r,bbox:i}=p(t,e,void 0,!0),n=i.width+e.padding+(i.height+e.padding),s=[{x:n/2,y:0},{x:n,y:-n/2},{x:n/2,y:-n},{x:0,y:-n/2}];a.l.info("Question main (Circle)");const l=y(r,n,n,s);return l.attr("style",e.style),g(e,l),e.intersect=function(t){return a.l.warn("Intersect called"),w.polygon(e,s,t)},r};function k(t,e,r,i){const n=[],s=t=>{n.push(t,0)},l=t=>{n.push(0,t)};e.includes("t")?(a.l.debug("add top border"),s(r)):l(r),e.includes("r")?(a.l.debug("add right border"),s(i)):l(i),e.includes("b")?(a.l.debug("add bottom border"),s(r)):l(r),e.includes("l")?(a.l.debug("add left border"),s(i)):l(i),t.attr("stroke-dasharray",n.join(" "))}const L=(t,e,r)=>{const a=t.insert("g").attr("class","node default").attr("id",e.domId||e.id);let i=70,n=10;"LR"===r&&(i=10,n=70);const s=a.append("rect").attr("x",-1*i/2).attr("y",-1*n/2).attr("width",i).attr("height",n).attr("class","fork-join");return g(e,s),e.height=e.height+e.padding/2,e.width=e.width+e.padding/2,e.intersect=function(t){return w.rect(e,t)},a},v={rhombus:m,question:m,rect:(t,e)=>{const{shapeSvg:r,bbox:i,halfPadding:n}=p(t,e,"node "+e.classes,!0),s=r.insert("rect",":first-child"),l=i.width+e.padding,d=i.height+e.padding;if(s.attr("class","basic label-container").attr("style",e.style).attr("rx",e.rx).attr("ry",e.ry).attr("x",-i.width/2-n).attr("y",-i.height/2-n).attr("width",l).attr("height",d),e.props){const t=new Set(Object.keys(e.props));e.props.borders&&(k(s,e.props.borders,l,d),t.delete("borders")),t.forEach((t=>{a.l.warn(`Unknown node property ${t}`)}))}return g(e,s),e.intersect=function(t){return w.rect(e,t)},r},labelRect:(t,e)=>{const{shapeSvg:r}=p(t,e,"label",!0);a.l.trace("Classes = ",e.classes);const i=r.insert("rect",":first-child");if(i.attr("width",0).attr("height",0),r.attr("class","label edgeLabel"),e.props){const t=new Set(Object.keys(e.props));e.props.borders&&(k(i,e.props.borders,0,0),t.delete("borders")),t.forEach((t=>{a.l.warn(`Unknown node property ${t}`)}))}return g(e,i),e.intersect=function(t){return w.rect(e,t)},r},rectWithTitle:(t,e)=>{let r;r=e.classes?"node "+e.classes:"node default";const n=t.insert("g").attr("class",r).attr("id",e.domId||e.id),s=n.insert("rect",":first-child"),l=n.insert("line"),d=n.insert("g").attr("class","label"),o=e.labelText.flat?e.labelText.flat():e.labelText;let c="";c="object"==typeof o?o[0]:o,a.l.info("Label text abc79",c,o,"object"==typeof o);const p=d.node().appendChild(h(c,e.labelStyle,!0,!0));let y={width:0,height:0};if((0,a.k)((0,a.g)().flowchart.htmlLabels)){const t=p.children[0],e=(0,i.Ys)(p);y=t.getBoundingClientRect(),e.attr("width",y.width),e.attr("height",y.height)}a.l.info("Text 2",o);const f=o.slice(1,o.length);let x=p.getBBox();const u=d.node().appendChild(h(f.join?f.join("<br/>"):f,e.labelStyle,!0,!0));if((0,a.k)((0,a.g)().flowchart.htmlLabels)){const t=u.children[0],e=(0,i.Ys)(u);y=t.getBoundingClientRect(),e.attr("width",y.width),e.attr("height",y.height)}const b=e.padding/2;return(0,i.Ys)(u).attr("transform","translate( "+(y.width>x.width?0:(x.width-y.width)/2)+", "+(x.height+b+5)+")"),(0,i.Ys)(p).attr("transform","translate( "+(y.width<x.width?0:-(x.width-y.width)/2)+", 0)"),y=d.node().getBBox(),d.attr("transform","translate("+-y.width/2+", "+(-y.height/2-b+3)+")"),s.attr("class","outer title-state").attr("x",-y.width/2-b).attr("y",-y.height/2-b).attr("width",y.width+e.padding).attr("height",y.height+e.padding),l.attr("class","divider").attr("x1",-y.width/2-b).attr("x2",y.width/2+b).attr("y1",-y.height/2-b+x.height+b).attr("y2",-y.height/2-b+x.height+b),g(e,s),e.intersect=function(t){return w.rect(e,t)},n},choice:(t,e)=>{const r=t.insert("g").attr("class","node default").attr("id",e.domId||e.id),a=[{x:0,y:14},{x:14,y:0},{x:0,y:-14},{x:-14,y:0}];return r.insert("polygon",":first-child").attr("points",a.map((function(t){return t.x+","+t.y})).join(" ")).attr("class","state-start").attr("r",7).attr("width",28).attr("height",28),e.width=28,e.height=28,e.intersect=function(t){return w.circle(e,14,t)},r},circle:(t,e)=>{const{shapeSvg:r,bbox:i,halfPadding:n}=p(t,e,void 0,!0),s=r.insert("circle",":first-child");return s.attr("style",e.style).attr("rx",e.rx).attr("ry",e.ry).attr("r",i.width/2+n).attr("width",i.width+e.padding).attr("height",i.height+e.padding),a.l.info("Circle main"),g(e,s),e.intersect=function(t){return a.l.info("Circle intersect",e,i.width/2+n,t),w.circle(e,i.width/2+n,t)},r},doublecircle:(t,e)=>{const{shapeSvg:r,bbox:i,halfPadding:n}=p(t,e,void 0,!0),s=r.insert("g",":first-child"),l=s.insert("circle"),d=s.insert("circle");return l.attr("style",e.style).attr("rx",e.rx).attr("ry",e.ry).attr("r",i.width/2+n+5).attr("width",i.width+e.padding+10).attr("height",i.height+e.padding+10),d.attr("style",e.style).attr("rx",e.rx).attr("ry",e.ry).attr("r",i.width/2+n).attr("width",i.width+e.padding).attr("height",i.height+e.padding),a.l.info("DoubleCircle main"),g(e,l),e.intersect=function(t){return a.l.info("DoubleCircle intersect",e,i.width/2+n+5,t),w.circle(e,i.width/2+n+5,t)},r},stadium:(t,e)=>{const{shapeSvg:r,bbox:a}=p(t,e,void 0,!0),i=a.height+e.padding,n=a.width+i/4+e.padding,s=r.insert("rect",":first-child").attr("style",e.style).attr("rx",i/2).attr("ry",i/2).attr("x",-n/2).attr("y",-i/2).attr("width",n).attr("height",i);return g(e,s),e.intersect=function(t){return w.rect(e,t)},r},hexagon:(t,e)=>{const{shapeSvg:r,bbox:a}=p(t,e,void 0,!0),i=a.height+e.padding,n=i/4,s=a.width+2*n+e.padding,l=[{x:n,y:0},{x:s-n,y:0},{x:s,y:-i/2},{x:s-n,y:-i},{x:n,y:-i},{x:0,y:-i/2}],d=y(r,s,i,l);return d.attr("style",e.style),g(e,d),e.intersect=function(t){return w.polygon(e,l,t)},r},rect_left_inv_arrow:(t,e)=>{const{shapeSvg:r,bbox:a}=p(t,e,void 0,!0),i=a.width+e.padding,n=a.height+e.padding,s=[{x:-n/2,y:0},{x:i,y:0},{x:i,y:-n},{x:-n/2,y:-n},{x:0,y:-n/2}];return y(r,i,n,s).attr("style",e.style),e.width=i+n,e.height=n,e.intersect=function(t){return w.polygon(e,s,t)},r},lean_right:(t,e)=>{const{shapeSvg:r,bbox:a}=p(t,e,void 0,!0),i=a.width+e.padding,n=a.height+e.padding,s=[{x:-2*n/6,y:0},{x:i-n/6,y:0},{x:i+2*n/6,y:-n},{x:n/6,y:-n}],l=y(r,i,n,s);return l.attr("style",e.style),g(e,l),e.intersect=function(t){return w.polygon(e,s,t)},r},lean_left:(t,e)=>{const{shapeSvg:r,bbox:a}=p(t,e,void 0,!0),i=a.width+e.padding,n=a.height+e.padding,s=[{x:2*n/6,y:0},{x:i+n/6,y:0},{x:i-2*n/6,y:-n},{x:-n/6,y:-n}],l=y(r,i,n,s);return l.attr("style",e.style),g(e,l),e.intersect=function(t){return w.polygon(e,s,t)},r},trapezoid:(t,e)=>{const{shapeSvg:r,bbox:a}=p(t,e,void 0,!0),i=a.width+e.padding,n=a.height+e.padding,s=[{x:-2*n/6,y:0},{x:i+2*n/6,y:0},{x:i-n/6,y:-n},{x:n/6,y:-n}],l=y(r,i,n,s);return l.attr("style",e.style),g(e,l),e.intersect=function(t){return w.polygon(e,s,t)},r},inv_trapezoid:(t,e)=>{const{shapeSvg:r,bbox:a}=p(t,e,void 0,!0),i=a.width+e.padding,n=a.height+e.padding,s=[{x:n/6,y:0},{x:i-n/6,y:0},{x:i+2*n/6,y:-n},{x:-2*n/6,y:-n}],l=y(r,i,n,s);return l.attr("style",e.style),g(e,l),e.intersect=function(t){return w.polygon(e,s,t)},r},rect_right_inv_arrow:(t,e)=>{const{shapeSvg:r,bbox:a}=p(t,e,void 0,!0),i=a.width+e.padding,n=a.height+e.padding,s=[{x:0,y:0},{x:i+n/2,y:0},{x:i,y:-n/2},{x:i+n/2,y:-n},{x:0,y:-n}],l=y(r,i,n,s);return l.attr("style",e.style),g(e,l),e.intersect=function(t){return w.polygon(e,s,t)},r},cylinder:(t,e)=>{const{shapeSvg:r,bbox:a}=p(t,e,void 0,!0),i=a.width+e.padding,n=i/2,s=n/(2.5+i/50),l=a.height+s+e.padding,d="M 0,"+s+" a "+n+","+s+" 0,0,0 "+i+" 0 a "+n+","+s+" 0,0,0 "+-i+" 0 l 0,"+l+" a "+n+","+s+" 0,0,0 "+i+" 0 l 0,"+-l,o=r.attr("label-offset-y",s).insert("path",":first-child").attr("style",e.style).attr("d",d).attr("transform","translate("+-i/2+","+-(l/2+s)+")");return g(e,o),e.intersect=function(t){const r=w.rect(e,t),a=r.x-e.x;if(0!=n&&(Math.abs(a)<e.width/2||Math.abs(a)==e.width/2&&Math.abs(r.y-e.y)>e.height/2-s)){let i=s*s*(1-a*a/(n*n));0!=i&&(i=Math.sqrt(i)),i=s-i,t.y-e.y>0&&(i=-i),r.y+=i}return r},r},start:(t,e)=>{const r=t.insert("g").attr("class","node default").attr("id",e.domId||e.id),a=r.insert("circle",":first-child");return a.attr("class","state-start").attr("r",7).attr("width",14).attr("height",14),g(e,a),e.intersect=function(t){return w.circle(e,7,t)},r},end:(t,e)=>{const r=t.insert("g").attr("class","node default").attr("id",e.domId||e.id),a=r.insert("circle",":first-child"),i=r.insert("circle",":first-child");return i.attr("class","state-start").attr("r",7).attr("width",14).attr("height",14),a.attr("class","state-end").attr("r",5).attr("width",10).attr("height",10),g(e,i),e.intersect=function(t){return w.circle(e,7,t)},r},note:(t,e)=>{e.useHtmlLabels||(0,a.g)().flowchart.htmlLabels||(e.centerLabel=!0);const{shapeSvg:r,bbox:i,halfPadding:n}=p(t,e,"node "+e.classes,!0);a.l.info("Classes = ",e.classes);const s=r.insert("rect",":first-child");return s.attr("rx",e.rx).attr("ry",e.ry).attr("x",-i.width/2-n).attr("y",-i.height/2-n).attr("width",i.width+e.padding).attr("height",i.height+e.padding),g(e,s),e.intersect=function(t){return w.rect(e,t)},r},subroutine:(t,e)=>{const{shapeSvg:r,bbox:a}=p(t,e,void 0,!0),i=a.width+e.padding,n=a.height+e.padding,s=[{x:0,y:0},{x:i,y:0},{x:i,y:-n},{x:0,y:-n},{x:0,y:0},{x:-8,y:0},{x:i+8,y:0},{x:i+8,y:-n},{x:-8,y:-n},{x:-8,y:0}],l=y(r,i,n,s);return l.attr("style",e.style),g(e,l),e.intersect=function(t){return w.polygon(e,s,t)},r},fork:L,join:L,class_box:(t,e)=>{const r=e.padding/2;let n;n=e.classes?"node "+e.classes:"node default";const s=t.insert("g").attr("class",n).attr("id",e.domId||e.id),d=s.insert("rect",":first-child"),o=s.insert("line"),c=s.insert("line");let p=0,y=4;const f=s.insert("g").attr("class","label");let x=0;const u=e.classData.annotations&&e.classData.annotations[0],b=e.classData.annotations[0]?"\xab"+e.classData.annotations[0]+"\xbb":"",m=f.node().appendChild(h(b,e.labelStyle,!0,!0));let k=m.getBBox();if((0,a.k)((0,a.g)().flowchart.htmlLabels)){const t=m.children[0],e=(0,i.Ys)(m);k=t.getBoundingClientRect(),e.attr("width",k.width),e.attr("height",k.height)}e.classData.annotations[0]&&(y+=k.height+4,p+=k.width);let L=e.classData.label;void 0!==e.classData.type&&""!==e.classData.type&&((0,a.g)().flowchart.htmlLabels?L+="&lt;"+e.classData.type+"&gt;":L+="<"+e.classData.type+">");const v=f.node().appendChild(h(L,e.labelStyle,!0,!0));(0,i.Ys)(v).attr("class","classTitle");let S=v.getBBox();if((0,a.k)((0,a.g)().flowchart.htmlLabels)){const t=v.children[0],e=(0,i.Ys)(v);S=t.getBoundingClientRect(),e.attr("width",S.width),e.attr("height",S.height)}y+=S.height+4,S.width>p&&(p=S.width);const M=[];e.classData.members.forEach((t=>{const r=(0,l.p)(t);let n=r.displayText;(0,a.g)().flowchart.htmlLabels&&(n=n.replace(/</g,"&lt;").replace(/>/g,"&gt;"));const s=f.node().appendChild(h(n,r.cssStyle?r.cssStyle:e.labelStyle,!0,!0));let d=s.getBBox();if((0,a.k)((0,a.g)().flowchart.htmlLabels)){const t=s.children[0],e=(0,i.Ys)(s);d=t.getBoundingClientRect(),e.attr("width",d.width),e.attr("height",d.height)}d.width>p&&(p=d.width),y+=d.height+4,M.push(s)})),y+=8;const T=[];if(e.classData.methods.forEach((t=>{const r=(0,l.p)(t);let n=r.displayText;(0,a.g)().flowchart.htmlLabels&&(n=n.replace(/</g,"&lt;").replace(/>/g,"&gt;"));const s=f.node().appendChild(h(n,r.cssStyle?r.cssStyle:e.labelStyle,!0,!0));let d=s.getBBox();if((0,a.k)((0,a.g)().flowchart.htmlLabels)){const t=s.children[0],e=(0,i.Ys)(s);d=t.getBoundingClientRect(),e.attr("width",d.width),e.attr("height",d.height)}d.width>p&&(p=d.width),y+=d.height+4,T.push(s)})),y+=8,u){let t=(p-k.width)/2;(0,i.Ys)(m).attr("transform","translate( "+(-1*p/2+t)+", "+-1*y/2+")"),x=k.height+4}let B=(p-S.width)/2;return(0,i.Ys)(v).attr("transform","translate( "+(-1*p/2+B)+", "+(-1*y/2+x)+")"),x+=S.height+4,o.attr("class","divider").attr("x1",-p/2-r).attr("x2",p/2+r).attr("y1",-y/2-r+8+x).attr("y2",-y/2-r+8+x),x+=8,M.forEach((t=>{(0,i.Ys)(t).attr("transform","translate( "+-p/2+", "+(-1*y/2+x+4)+")"),x+=S.height+4})),x+=8,c.attr("class","divider").attr("x1",-p/2-r).attr("x2",p/2+r).attr("y1",-y/2-r+8+x).attr("y2",-y/2-r+8+x),x+=8,T.forEach((t=>{(0,i.Ys)(t).attr("transform","translate( "+-p/2+", "+(-1*y/2+x)+")"),x+=S.height+4})),d.attr("class","outer title-state").attr("x",-p/2-r).attr("y",-y/2-r).attr("width",p+e.padding).attr("height",y+e.padding),g(e,d),e.intersect=function(t){return w.rect(e,t)},s}};let S={};const M=(t,e,r)=>{let i,n;if(e.link){let s;"sandbox"===(0,a.g)().securityLevel?s="_top":e.linkTarget&&(s=e.linkTarget||"_blank"),i=t.insert("svg:a").attr("xlink:href",e.link).attr("target",s),n=v[e.shape](i,e,r)}else n=v[e.shape](t,e,r),i=n;return e.tooltip&&n.attr("title",e.tooltip),e.class&&n.attr("class","node default "+e.class),S[e.id]=i,e.haveCallback&&S[e.id].attr("class",S[e.id].attr("class")+" clickable"),i},T=(t,e)=>{S[e.id]=t},B=()=>{S={}},E=t=>{const e=S[t.id];a.l.trace("Transforming node",t.diff,t,"translate("+(t.x-t.width/2-5)+", "+t.width/2+")");const r=t.diff||0;return t.clusterNode?e.attr("transform","translate("+(t.x+r-t.width/2)+", "+(t.y-t.height/2-8)+")"):e.attr("transform","translate("+t.x+", "+t.y+")"),r};let C={},Y={};const N=()=>{C={},Y={}},P=(t,e)=>{const r=(0,a.k)((0,a.g)().flowchart.htmlLabels),s="markdown"===e.labelType?(0,n.c)(t,e.label,{style:e.labelStyle,useHtmlLabels:r,addSvgBackground:!0}):h(e.label,e.labelStyle);a.l.info("abc82",e,e.labelType);const l=t.insert("g").attr("class","edgeLabel"),d=l.insert("g").attr("class","label");d.node().appendChild(s);let o,c=s.getBBox();if(r){const t=s.children[0],e=(0,i.Ys)(s);c=t.getBoundingClientRect(),e.attr("width",c.width),e.attr("height",c.height)}if(d.attr("transform","translate("+-c.width/2+", "+-c.height/2+")"),C[e.id]=l,e.width=c.width,e.height=c.height,e.startLabelLeft){const r=h(e.startLabelLeft,e.labelStyle),a=t.insert("g").attr("class","edgeTerminals"),i=a.insert("g").attr("class","inner");o=i.node().appendChild(r);const n=r.getBBox();i.attr("transform","translate("+-n.width/2+", "+-n.height/2+")"),Y[e.id]||(Y[e.id]={}),Y[e.id].startLeft=a,_(o,e.startLabelLeft)}if(e.startLabelRight){const r=h(e.startLabelRight,e.labelStyle),a=t.insert("g").attr("class","edgeTerminals"),i=a.insert("g").attr("class","inner");o=a.node().appendChild(r),i.node().appendChild(r);const n=r.getBBox();i.attr("transform","translate("+-n.width/2+", "+-n.height/2+")"),Y[e.id]||(Y[e.id]={}),Y[e.id].startRight=a,_(o,e.startLabelRight)}if(e.endLabelLeft){const r=h(e.endLabelLeft,e.labelStyle),a=t.insert("g").attr("class","edgeTerminals"),i=a.insert("g").attr("class","inner");o=i.node().appendChild(r);const n=r.getBBox();i.attr("transform","translate("+-n.width/2+", "+-n.height/2+")"),a.node().appendChild(r),Y[e.id]||(Y[e.id]={}),Y[e.id].endLeft=a,_(o,e.endLabelLeft)}if(e.endLabelRight){const r=h(e.endLabelRight,e.labelStyle),a=t.insert("g").attr("class","edgeTerminals"),i=a.insert("g").attr("class","inner");o=i.node().appendChild(r);const n=r.getBBox();i.attr("transform","translate("+-n.width/2+", "+-n.height/2+")"),a.node().appendChild(r),Y[e.id]||(Y[e.id]={}),Y[e.id].endRight=a,_(o,e.endLabelRight)}return s};function _(t,e){(0,a.g)().flowchart.htmlLabels&&t&&(t.style.width=9*e.length+"px",t.style.height="12px")}const H=(t,e)=>{a.l.info("Moving label abc78 ",t.id,t.label,C[t.id]);let r=e.updatedPath?e.updatedPath:e.originalPath;if(t.label){const i=C[t.id];let n=t.x,s=t.y;if(r){const i=d.u.calcLabelPosition(r);a.l.info("Moving label "+t.label+" from (",n,",",s,") to (",i.x,",",i.y,") abc78"),e.updatedPath&&(n=i.x,s=i.y)}i.attr("transform","translate("+n+", "+s+")")}if(t.startLabelLeft){const e=Y[t.id].startLeft;let a=t.x,i=t.y;if(r){const e=d.u.calcTerminalLabelPosition(t.arrowTypeStart?10:0,"start_left",r);a=e.x,i=e.y}e.attr("transform","translate("+a+", "+i+")")}if(t.startLabelRight){const e=Y[t.id].startRight;let a=t.x,i=t.y;if(r){const e=d.u.calcTerminalLabelPosition(t.arrowTypeStart?10:0,"start_right",r);a=e.x,i=e.y}e.attr("transform","translate("+a+", "+i+")")}if(t.endLabelLeft){const e=Y[t.id].endLeft;let a=t.x,i=t.y;if(r){const e=d.u.calcTerminalLabelPosition(t.arrowTypeEnd?10:0,"end_left",r);a=e.x,i=e.y}e.attr("transform","translate("+a+", "+i+")")}if(t.endLabelRight){const e=Y[t.id].endRight;let a=t.x,i=t.y;if(r){const e=d.u.calcTerminalLabelPosition(t.arrowTypeEnd?10:0,"end_right",r);a=e.x,i=e.y}e.attr("transform","translate("+a+", "+i+")")}},R=(t,e)=>{a.l.warn("abc88 cutPathAtIntersect",t,e);let r=[],i=t[0],n=!1;return t.forEach((t=>{if(a.l.info("abc88 checking point",t,e),((t,e)=>{const r=t.x,a=t.y,i=Math.abs(e.x-r),n=Math.abs(e.y-a),s=t.width/2,l=t.height/2;return i>=s||n>=l})(e,t)||n)a.l.warn("abc88 outside",t,i),i=t,n||r.push(t);else{const s=((t,e,r)=>{a.l.warn(`intersection calc abc89:\n  outsidePoint: ${JSON.stringify(e)}\n  insidePoint : ${JSON.stringify(r)}\n  node        : x:${t.x} y:${t.y} w:${t.width} h:${t.height}`);const i=t.x,n=t.y,s=Math.abs(i-r.x),l=t.width/2;let d=r.x<e.x?l-s:l+s;const o=t.height/2,c=Math.abs(e.y-r.y),h=Math.abs(e.x-r.x);if(Math.abs(n-e.y)*l>Math.abs(i-e.x)*o){let t=r.y<e.y?e.y-o-n:n-o-e.y;d=h*t/c;const i={x:r.x<e.x?r.x+d:r.x-h+d,y:r.y<e.y?r.y+c-t:r.y-c+t};return 0===d&&(i.x=e.x,i.y=e.y),0===h&&(i.x=e.x),0===c&&(i.y=e.y),a.l.warn(`abc89 topp/bott calc, Q ${c}, q ${t}, R ${h}, r ${d}`,i),i}{d=r.x<e.x?e.x-l-i:i-l-e.x;let t=c*d/h,n=r.x<e.x?r.x+h-d:r.x-h+d,s=r.y<e.y?r.y+t:r.y-t;return a.l.warn(`sides calc abc89, Q ${c}, q ${t}, R ${h}, r ${d}`,{_x:n,_y:s}),0===d&&(n=e.x,s=e.y),0===h&&(n=e.x),0===c&&(s=e.y),{x:n,y:s}}})(e,i,t);a.l.warn("abc88 inside",t,i,s),a.l.warn("abc88 intersection",s);let l=!1;r.forEach((t=>{l=l||t.x===s.x&&t.y===s.y})),r.some((t=>t.x===s.x&&t.y===s.y))?a.l.warn("abc88 no intersect",s,r):r.push(s),n=!0}})),a.l.warn("abc88 returning points",r),r},I=function(t,e,r,n,s,l){let d=r.points,o=!1;const c=l.node(e.v);var h=l.node(e.w);a.l.info("abc88 InsertEdge: ",r),h.intersect&&c.intersect&&(d=d.slice(1,r.points.length-1),d.unshift(c.intersect(d[0])),a.l.info("Last point",d[d.length-1],h,h.intersect(d[d.length-1])),d.push(h.intersect(d[d.length-1]))),r.toCluster&&(a.l.info("to cluster abc88",n[r.toCluster]),d=R(r.points,n[r.toCluster].node),o=!0),r.fromCluster&&(a.l.info("from cluster abc88",n[r.fromCluster]),d=R(d.reverse(),n[r.fromCluster].node).reverse(),o=!0);const p=d.filter((t=>!Number.isNaN(t.y)));let g;g=("graph"===s||"flowchart"===s)&&r.curve||i.$0Z;const y=(0,i.jvg)().x((function(t){return t.x})).y((function(t){return t.y})).curve(g);let f;switch(r.thickness){case"normal":f="edge-thickness-normal";break;case"thick":case"invisible":f="edge-thickness-thick";break;default:f=""}switch(r.pattern){case"solid":f+=" edge-pattern-solid";break;case"dotted":f+=" edge-pattern-dotted";break;case"dashed":f+=" edge-pattern-dashed"}const x=t.append("path").attr("d",y(p)).attr("id",r.id).attr("class"," "+f+(r.classes?" "+r.classes:"")).attr("style",r.style);let u="";switch(((0,a.g)().flowchart.arrowMarkerAbsolute||(0,a.g)().state.arrowMarkerAbsolute)&&(u=window.location.protocol+"//"+window.location.host+window.location.pathname+window.location.search,u=u.replace(/\(/g,"\\("),u=u.replace(/\)/g,"\\)")),a.l.info("arrowTypeStart",r.arrowTypeStart),a.l.info("arrowTypeEnd",r.arrowTypeEnd),r.arrowTypeStart){case"arrow_cross":x.attr("marker-start","url("+u+"#"+s+"-crossStart)");break;case"arrow_point":x.attr("marker-start","url("+u+"#"+s+"-pointStart)");break;case"arrow_barb":x.attr("marker-start","url("+u+"#"+s+"-barbStart)");break;case"arrow_circle":x.attr("marker-start","url("+u+"#"+s+"-circleStart)");break;case"aggregation":x.attr("marker-start","url("+u+"#"+s+"-aggregationStart)");break;case"extension":x.attr("marker-start","url("+u+"#"+s+"-extensionStart)");break;case"composition":x.attr("marker-start","url("+u+"#"+s+"-compositionStart)");break;case"dependency":x.attr("marker-start","url("+u+"#"+s+"-dependencyStart)");break;case"lollipop":x.attr("marker-start","url("+u+"#"+s+"-lollipopStart)")}switch(r.arrowTypeEnd){case"arrow_cross":x.attr("marker-end","url("+u+"#"+s+"-crossEnd)");break;case"arrow_point":x.attr("marker-end","url("+u+"#"+s+"-pointEnd)");break;case"arrow_barb":x.attr("marker-end","url("+u+"#"+s+"-barbEnd)");break;case"arrow_circle":x.attr("marker-end","url("+u+"#"+s+"-circleEnd)");break;case"aggregation":x.attr("marker-end","url("+u+"#"+s+"-aggregationEnd)");break;case"extension":x.attr("marker-end","url("+u+"#"+s+"-extensionEnd)");break;case"composition":x.attr("marker-end","url("+u+"#"+s+"-compositionEnd)");break;case"dependency":x.attr("marker-end","url("+u+"#"+s+"-dependencyEnd)");break;case"lollipop":x.attr("marker-end","url("+u+"#"+s+"-lollipopEnd)")}let b={};return o&&(b.updatedPath=d),b.originalPath=r.points,b}},7657:(t,e,r)=>{r.d(e,{p:()=>l,s:()=>g});var a=r(4646),i=r(1188),n=r(44);let s=0;const l=function(t){let e=t.match(/^([#+~-])?(\w+)(~\w+~|\[])?\s+(\w+) *([$*])?$/),r=t.match(/^([#+|~-])?(\w+) *\( *(.*)\) *([$*])? *(\w*[[\]|~]*\s*\w*~?)$/);return e&&!r?d(e):r?o(r):c(t)},d=function(t){let e="",r="";try{let a=t[1]?t[1].trim():"",i=t[2]?t[2].trim():"",s=t[3]?(0,n.z)(t[3].trim()):"",l=t[4]?t[4].trim():"",d=t[5]?t[5].trim():"";r=a+i+s+" "+l,e=p(d)}catch(a){r=t}return{displayText:r,cssStyle:e}},o=function(t){let e="",r="";try{let a=t[1]?t[1].trim():"",i=t[2]?t[2].trim():"",s=t[3]?(0,n.z)(t[3].trim()):"",l=t[4]?t[4].trim():"";r=a+i+"("+s+")"+(t[5]?" : "+(0,n.z)(t[5]).trim():""),e=p(l)}catch(a){r=t}return{displayText:r,cssStyle:e}},c=function(t){let e="",r="",a="",i=t.indexOf("("),s=t.indexOf(")");if(i>1&&s>i&&s<=t.length){let l="",d="",o=t.substring(0,1);o.match(/\w/)?d=t.substring(0,i).trim():(o.match(/[#+~-]/)&&(l=o),d=t.substring(1,i).trim());const c=t.substring(i+1,s);t.substring(s+1,1),r=p(t.substring(s+1,s+2)),e=l+d+"("+(0,n.z)(c.trim())+")",s<t.length&&(a=t.substring(s+2).trim(),""!==a&&(a=" : "+(0,n.z)(a),e+=a))}else e=(0,n.z)(t);return{displayText:e,cssStyle:r}},h=function(t,e,r,a){let i=l(e);const n=t.append("tspan").attr("x",a.padding).text(i.displayText);""!==i.cssStyle&&n.attr("style",i.cssStyle),r||n.attr("dy",a.textHeight)},p=function(t){switch(t){case"*":return"font-style:italic;";case"$":return"text-decoration:underline;";default:return""}},g={drawClass:function(t,e,r,a){n.l.debug("Rendering class ",e,r);const i=e.id,s={id:i,label:e.id,width:0,height:0},l=t.append("g").attr("id",a.db.lookUpDomId(i)).attr("class","classGroup");let d;d=e.link?l.append("svg:a").attr("xlink:href",e.link).attr("target",e.linkTarget).append("text").attr("y",r.textHeight+r.padding).attr("x",0):l.append("text").attr("y",r.textHeight+r.padding).attr("x",0);let o=!0;e.annotations.forEach((function(t){const e=d.append("tspan").text("\xab"+t+"\xbb");o||e.attr("dy",r.textHeight),o=!1}));let c=e.id;void 0!==e.type&&""!==e.type&&(c+="<"+e.type+">");const p=d.append("tspan").text(c).attr("class","title");o||p.attr("dy",r.textHeight);const g=d.node().getBBox().height,y=l.append("line").attr("x1",0).attr("y1",r.padding+g+r.dividerMargin/2).attr("y2",r.padding+g+r.dividerMargin/2),f=l.append("text").attr("x",r.padding).attr("y",g+r.dividerMargin+r.textHeight).attr("fill","white").attr("class","classText");o=!0,e.members.forEach((function(t){h(f,t,o,r),o=!1}));const x=f.node().getBBox(),u=l.append("line").attr("x1",0).attr("y1",r.padding+g+r.dividerMargin+x.height).attr("y2",r.padding+g+r.dividerMargin+x.height),b=l.append("text").attr("x",r.padding).attr("y",g+2*r.dividerMargin+x.height+r.textHeight).attr("fill","white").attr("class","classText");o=!0,e.methods.forEach((function(t){h(b,t,o,r),o=!1}));const w=l.node().getBBox();var m=" ";e.cssClasses.length>0&&(m+=e.cssClasses.join(" "));const k=l.insert("rect",":first-child").attr("x",0).attr("y",0).attr("width",w.width+2*r.padding).attr("height",w.height+r.padding+.5*r.dividerMargin).attr("class",m).node().getBBox().width;return d.node().childNodes.forEach((function(t){t.setAttribute("x",(k-t.getBBox().width)/2)})),e.tooltip&&d.insert("title").text(e.tooltip),y.attr("x2",k),u.attr("x2",k),s.width=k,s.height=w.height+r.padding+.5*r.dividerMargin,s},drawEdge:function(t,e,r,l,d){const o=function(t){switch(t){case d.db.relationType.AGGREGATION:return"aggregation";case d.db.relationType.EXTENSION:return"extension";case d.db.relationType.COMPOSITION:return"composition";case d.db.relationType.DEPENDENCY:return"dependency";case d.db.relationType.LOLLIPOP:return"lollipop"}};e.points=e.points.filter((t=>!Number.isNaN(t.y)));const c=e.points,h=(0,a.jvg)().x((function(t){return t.x})).y((function(t){return t.y})).curve(a.$0Z),p=t.append("path").attr("d",h(c)).attr("id","edge"+s).attr("class","relation");let g,y,f="";l.arrowMarkerAbsolute&&(f=window.location.protocol+"//"+window.location.host+window.location.pathname+window.location.search,f=f.replace(/\(/g,"\\("),f=f.replace(/\)/g,"\\)")),1==r.relation.lineType&&p.attr("class","relation dashed-line"),10==r.relation.lineType&&p.attr("class","relation dotted-line"),"none"!==r.relation.type1&&p.attr("marker-start","url("+f+"#"+o(r.relation.type1)+"Start)"),"none"!==r.relation.type2&&p.attr("marker-end","url("+f+"#"+o(r.relation.type2)+"End)");const x=e.points.length;let u,b,w,m,k=i.u.calcLabelPosition(e.points);if(g=k.x,y=k.y,x%2!=0&&x>1){let t=i.u.calcCardinalityPosition("none"!==r.relation.type1,e.points,e.points[0]),a=i.u.calcCardinalityPosition("none"!==r.relation.type2,e.points,e.points[x-1]);n.l.debug("cardinality_1_point "+JSON.stringify(t)),n.l.debug("cardinality_2_point "+JSON.stringify(a)),u=t.x,b=t.y,w=a.x,m=a.y}if(void 0!==r.title){const e=t.append("g").attr("class","classLabel"),a=e.append("text").attr("class","label").attr("x",g).attr("y",y).attr("fill","red").attr("text-anchor","middle").text(r.title);window.label=a;const i=a.node().getBBox();e.insert("rect",":first-child").attr("class","box").attr("x",i.x-l.padding/2).attr("y",i.y-l.padding/2).attr("width",i.width+l.padding).attr("height",i.height+l.padding)}if(n.l.info("Rendering relation "+JSON.stringify(r)),void 0!==r.relationTitle1&&"none"!==r.relationTitle1){t.append("g").attr("class","cardinality").append("text").attr("class","type1").attr("x",u).attr("y",b).attr("fill","black").attr("font-size","6").text(r.relationTitle1)}if(void 0!==r.relationTitle2&&"none"!==r.relationTitle2){t.append("g").attr("class","cardinality").append("text").attr("class","type2").attr("x",w).attr("y",m).attr("fill","black").attr("font-size","6").text(r.relationTitle2)}s++},drawNote:function(t,e,r,a){n.l.debug("Rendering note ",e,r);const i=e.id,s={id:i,text:e.text,width:0,height:0},l=t.append("g").attr("id",i).attr("class","classGroup");let d=l.append("text").attr("y",r.textHeight+r.padding).attr("x",0);const o=JSON.parse(`"${e.text}"`).split("\n");o.forEach((function(t){n.l.debug(`Adding line: ${t}`),d.append("tspan").text(t).attr("class","title").attr("dy",r.textHeight)}));const c=l.node().getBBox(),h=l.insert("rect",":first-child").attr("x",0).attr("y",0).attr("width",c.width+2*r.padding).attr("height",c.height+o.length*r.textHeight+r.padding+.5*r.dividerMargin).node().getBBox().width;return d.node().childNodes.forEach((function(t){t.setAttribute("x",(h-t.getBBox().width)/2)})),s.width=h,s.height=c.height+o.length*r.textHeight+r.padding+.5*r.dividerMargin,s},parseMember:l}}}]);