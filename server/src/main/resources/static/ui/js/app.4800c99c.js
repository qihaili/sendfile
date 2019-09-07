(function(e){function t(t){for(var s,o,r=t[0],l=t[1],d=t[2],p=0,u=[];p<r.length;p++)o=r[p],i[o]&&u.push(i[o][0]),i[o]=0;for(s in l)Object.prototype.hasOwnProperty.call(l,s)&&(e[s]=l[s]);c&&c(t);while(u.length)u.shift()();return n.push.apply(n,d||[]),a()}function a(){for(var e,t=0;t<n.length;t++){for(var a=n[t],s=!0,r=1;r<a.length;r++){var l=a[r];0!==i[l]&&(s=!1)}s&&(n.splice(t--,1),e=o(o.s=a[0]))}return e}var s={},i={app:0},n=[];function o(t){if(s[t])return s[t].exports;var a=s[t]={i:t,l:!1,exports:{}};return e[t].call(a.exports,a,a.exports,o),a.l=!0,a.exports}o.m=e,o.c=s,o.d=function(e,t,a){o.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:a})},o.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},o.t=function(e,t){if(1&t&&(e=o(e)),8&t)return e;if(4&t&&"object"===typeof e&&e&&e.__esModule)return e;var a=Object.create(null);if(o.r(a),Object.defineProperty(a,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var s in e)o.d(a,s,function(t){return e[t]}.bind(null,s));return a},o.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return o.d(t,"a",t),t},o.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},o.p="/ui/";var r=window["webpackJsonp"]=window["webpackJsonp"]||[],l=r.push.bind(r);r.push=t,r=r.slice();for(var d=0;d<r.length;d++)t(r[d]);var c=l;n.push([0,"chunk-vendors"]),a()})({0:function(e,t,a){e.exports=a("56d7")},"034f":function(e,t,a){"use strict";var s=a("64a9"),i=a.n(s);i.a},"3fab":function(e,t,a){"use strict";a.r(t),a.d(t,"msg",function(){return s});var s={languageName:"English",upload:{dragFile:{1:"Drag files here, or ",2:"click to add"},tip:{normal:"Can upload {size} files",anySize:"Can upload any size files"},expire:"Expires after",enablePassword:"Enable password",uploadButton:"Upload",wrongToken:"Token authorization failed"},uploaded:{downloadLink:"Download link：",copyLinkButton:"Copy",tip:"Copy link and open in browser.",backToHomeLink:"Upload again"},message:{uploading:"Uploading. Will generate download link after success.",uploadSuccess:"Upload success",uploadFail:"Upload failed. {errMsg}",fileTooBig:"File is too big, cannot exceed {size}",linkCopied:"Link copied"},expireTip:{normal:"Files will expire after {ttl}",neverExpire:"Files will never expire."},durationUnit:{day:"d ",hour:"h ",minute:"m ",D:" Day(s)",H:" Hour(s)",M:" Minute(s)"},download:{title:"File (click file name to download)",needPassword:"Files are encrypted, please input password",unlockButton:"Unlock",gotoUploadLink:"Use SendFile to share files",fileExpired:"Files are expired",wrongPassword:"Wront password"},files:{copyLink:"Copy link",copyPassword:"Copy password",deleteFile:"Delete files",linkCopied:"Link copied",passwordCopied:"Password copied",confirmDelete:"You are going to delete these files. Are you sure?",okButton:"OK",cancelButton:"Cancel"}}},"41f3":function(e,t,a){"use strict";var s=a("bd16"),i=a.n(s);i.a},"56d7":function(e,t,a){"use strict";a.r(t);a("cadf"),a("551c"),a("f751"),a("097d");var s=a("2b0e"),i=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{attrs:{id:"app"}},[a("el-row",{staticStyle:{"margin-bottom":"30px"}},[a("div",[a("i",{staticClass:"el-icon-s-promotion",staticStyle:{"margin-right":"20px","font-size":"60px",color:"#409EFF","vertical-align":"middle"}}),a("span",{staticStyle:{"font-size":"40px","font-weight":"bold",color:"#606266","vertical-align":"middle"}},[e._v("Send File")])]),a("div",{staticStyle:{position:"absolute",right:"100px",bottom:"0px"}},[a("div",[a("el-dropdown",{on:{command:e.changeLang}},[a("span",{staticStyle:{cursor:"pointer","font-size":"medium"}},[e._v("\n          "+e._s(e.$t("msg.languageName"))),a("i",{staticClass:"el-icon-arrow-down el-icon--right"})]),a("el-dropdown-menu",{attrs:{slot:"dropdown"},slot:"dropdown"},e._l(e.$i18n.messages,function(t,s){return a("el-dropdown-item",{key:s,attrs:{command:s,disabled:e.$i18n.locale==s}},[e._v(e._s(t.msg.languageName))])}),1)],1)],1)])]),a("router-view")],1)},n=[],o={name:"app",methods:{changeLang:function(e){this.$i18n.locale=e}}},r=o,l=(a("034f"),a("2877")),d=Object(l["a"])(r,i,n,!1,null,null,null),c=d.exports,p=a("8c4f"),u=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("el-card",{staticClass:"mycard"},[e.errorMsg?a("div",{staticStyle:{height:"310px",width:"100%",display:"table","text-align":"center"}},[a("p",{staticStyle:{display:"table-cell","vertical-align":"middle",color:"#F56C6C","font-size":"large"}},[a("i",{staticClass:"el-icon-warning",staticStyle:{"margin-right":"5px"}}),e.errorMsg.msg?a("span",[e._v(e._s(e.$t(e.errorMsg.msg)))]):a("span",[e._v(e._s(e.errorMsg))])])]):e.isChooseUpload?a("div",[a("el-progress",{staticStyle:{"margin-top":"20px"},attrs:{type:"circle","stroke-width":18,percentage:e.uploadPercentage,status:e.uploadStatus}}),a("div",{staticStyle:{height:"20px","line-height":"20px"}},[a("span",{staticStyle:{"font-size":"small"}},[e._v(e._s(e.speed))])]),e.share?a("div",{staticStyle:{"text-align":"left",position:"relative",height:"280px"}},[e.config?a("span",{staticStyle:{"font-size":"small"}},[a("humanReadableDuration",{attrs:{ttl:this.share.ttl}})],1):e._e(),a("div",{staticStyle:{margin:"20px 5px"}},[a("el-row",[e._v(e._s(e.$t("msg.uploaded.downloadLink")))]),a("el-input",{attrs:{value:e.address}},[a("el-button",{directives:[{name:"clipboard",rawName:"v-clipboard:copy",value:e.address,expression:"address",arg:"copy"},{name:"clipboard",rawName:"v-clipboard:success",value:e.onCopySuccess,expression:"onCopySuccess",arg:"success"}],attrs:{slot:"append",type:"primary",size:"mini"},slot:"append"},[e._v(e._s(e.$t("msg.uploaded.copyLinkButton")))])],1),a("el-row",{staticStyle:{"font-size":"small"}},[a("i",{staticClass:"el-icon-warning",staticStyle:{"margin-right":"5px"}}),e._v(e._s(e.$t("msg.uploaded.tip")))])],1),a("div",{staticStyle:{position:"absolute",bottom:"0px","text-align":"center",width:"100%"}},[a("el-link",{attrs:{type:"primary",icon:"el-icon-s-home"},on:{click:e.backToHome}},[e._v(e._s(e.$t("msg.uploaded.backToHomeLink")))])],1)]):a("div",{staticStyle:{margin:"50px 5px"}},[a("span",[e._v(e._s(e.$t("msg.message.uploading")))])])],1):a("div",[a("div",{staticStyle:{"text-align":"left",width:"358px",margin:"auto"}},[a("el-upload",{staticStyle:{"text-align":"left","min-height":"310px"},attrs:{drag:"",action:"","show-file-list":!0,"auto-upload":!1,multiple:!0,"on-change":e.onChange}},[a("i",{staticClass:"el-icon-circle-plus",staticStyle:{"font-size":"67px",color:"#C0C4CC",margin:"40px 0 16px","line-height":"50px"}}),a("div",{staticClass:"el-upload__text"},[e._v(e._s(e.$t("msg.upload.dragFile.1"))),a("em",[e._v(e._s(e.$t("msg.upload.dragFile.2")))])]),e.config?a("div",{staticClass:"el-upload__tip",staticStyle:{"text-align":"center"},attrs:{slot:"tip"},slot:"tip"},[e.config.share.maxFileSize.startsWith("-1")?a("span",[e._v(e._s(e.$t("msg.upload.tip.anySize")))]):a("span",[e._v(e._s(e.$t("msg.upload.tip.normal",{size:this.config.share.maxFileSize})))])]):e._e()]),a("el-row",{staticStyle:{"margin-top":"5px","line-height":"42px"}},["en-US"==e.$i18n.locale?a("span",{staticStyle:{color:"#606266"}},[e._v(e._s(e.$t("msg.upload.expire")))]):e._e(),e.config?a("el-select",{staticStyle:{width:"150px",margin:"0px 10px 0px 10px"},model:{value:e.ttl,callback:function(t){e.ttl=t},expression:"ttl"}},e._l(e.config.share.ttlOptions,function(t,s){return a("el-option",{key:s,attrs:{label:t.value.slice(0,t.value.length-1)+e.$t("msg.durationUnit."+t.value.charAt(t.value.length-1)),value:t.value}})}),1):e._e(),"zh-CN"==e.$i18n.locale?a("span",{staticStyle:{color:"#606266"}},[e._v(e._s(e.$t("msg.upload.expire")))]):e._e()],1),a("el-row",{staticStyle:{"margin-top":"5px","line-height":"42px"}},[a("el-checkbox",{model:{value:e.passwordEnabled,callback:function(t){e.passwordEnabled=t},expression:"passwordEnabled"}},[e._v(e._s(e.$t("msg.upload.enablePassword")))]),e.passwordEnabled?a("el-input",{staticStyle:{width:"200px","margin-left":"10px"},attrs:{autofocus:"",type:e.showPassword?"text":"password"},model:{value:e.password,callback:function(t){e.password=t},expression:"password"}},[a("i",{class:"el-input__icon iconfont "+(e.showPassword?"icon-eye-open":"icon-eye-close"),staticStyle:{cursor:"pointer"},attrs:{slot:"suffix"},on:{click:function(t){e.showPassword=!e.showPassword}},slot:"suffix"})]):e._e()],1),a("el-button",{staticStyle:{width:"100%","margin-top":"10px"},attrs:{type:"primary",icon:"el-icon-upload",disabled:null===e.fileList||0==e.fileList.length},on:{click:e.submitUpload}},[e._v(e._s(e.$t("msg.upload.uploadButton")))])],1)])]),a("transition",[e.uploadedList&&e.uploadedList.length>0?a("el-card",{staticClass:"mycard"},[a("transition-group",{attrs:{name:"list"}},e._l(e.uploadedList,function(t){return a("el-card",{key:t.id,staticStyle:{margin:"10px 0px"}},[a("files",{attrs:{share:t,"on-removed":e.shareRemoved}})],1)}),1)],1):e._e()],1)],1)},h=[],g=a("7618"),f=(a("ac4d"),a("8a81"),a("ac6a"),a("96cf"),a("3b8d")),m=a("bc3a"),v=a.n(m),y=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[e._l(e.share.files,function(t,s){return a("el-row",{key:s},[a("div",{staticStyle:{display:"inline-block",float:"left"}},[a("el-tooltip",{attrs:{content:t.name,placement:"top"}},[a("el-link",{staticStyle:{"vertical-align":"middle","font-size":"large",margin:"0px"},attrs:{href:"/api/shares/"+e.share.id+"/"+t.name,icon:"el-icon-document"}},[a("div",{staticStyle:{"max-width":"340px","text-align":"left",overflow:"hidden","white-space":"nowrap","text-overflow":"ellipsis"}},[e._v(e._s(t.name))])])],1)],1),a("div",{staticStyle:{display:"inline-block",float:"right"}},[a("span",{staticStyle:{"vertical-align":"middle","font-size":"small"}},[e._v(e._s(e.humanReadableDataSize(t.size)))])])])}),a("div",{staticClass:"el-divider el-divider--horizontal",staticStyle:{margin:"10px 0px"}}),a("el-row",[a("div",{staticStyle:{display:"inline-block",float:"left","text-align":"left"}},[a("el-row",[a("span",{staticStyle:{"vertical-align":"middle","font-size":"small"}},[a("humanReadableDuration",{attrs:{ttl:this.share.ttl}})],1)])],1),a("div",{staticStyle:{display:"inline-block",float:"right"}},[a("el-tooltip",{attrs:{content:e.$t("msg.files.copyLink"),placement:"top"}},[a("el-button",{directives:[{name:"clipboard",rawName:"v-clipboard:copy",value:e.address,expression:"address",arg:"copy"},{name:"clipboard",rawName:"v-clipboard:success",value:e.onCopySuccess,expression:"onCopySuccess",arg:"success"}],attrs:{size:"mini",icon:"el-icon-document-copy"}})],1),e.share.password?a("el-tooltip",{attrs:{content:e.$t("msg.files.copyPassword"),placement:"top"}},[a("el-button",{directives:[{name:"clipboard",rawName:"v-clipboard:copy",value:e.share.password,expression:"share.password",arg:"copy"},{name:"clipboard",rawName:"v-clipboard:success",value:e.onCopyPasswordSuccess,expression:"onCopyPasswordSuccess",arg:"success"}],attrs:{size:"mini",icon:"el-icon-key"}})],1):e._e(),e.share.token?a("el-tooltip",{attrs:{content:e.$t("msg.files.deleteFile"),placement:"top"}},[a("el-button",{attrs:{type:"danger",size:"mini",icon:"el-icon-delete",loading:e.deleteLoading},on:{click:e.deleteShare}})],1):e._e()],1)])],2)},w=[],x=(a("6b54"),function(){var e=this,t=e.$createElement,a=e._self._c||t;return this.ttl?a("span",[e._v(e._s(e.$t("msg.expireTip.normal",{ttl:(0==e.duration.day?"":e.duration.day+e.$t("msg.durationUnit.day"))+(0==e.duration.hour?"":e.duration.hour+e.$t("msg.durationUnit.hour"))+e.duration.minute+e.$t("msg.durationUnit.minute")})))]):a("span",[e._v(e._s(e.$t("msg.expireTip.neverExpire")))])}),b=[],S=(a("c5f6"),{props:{ttl:{type:Number}},computed:{duration:function(){return this.getDuration(this.ttl)}},methods:{getDuration:function(e){var t={day:0,hour:0,minute:0};return e>864e5&&(t.day=parseInt(e/864e5),e%=864e5),e>36e5&&(t.hour=parseInt(e/36e5),e%=36e5),e>6e4&&(t.minute=parseInt(e/6e4)),t}}}),k=S,_=Object(l["a"])(k,x,b,!1,null,null,null),$=_.exports,C=a("5118"),L={props:{share:{type:Object},onRemoved:{type:Function,required:!0}},components:{humanReadableDuration:$},data:function(){return{address:window.location.href+("/"==this.$route.path?this.share.id:""),syncSharePolling:null,countdown:null,deleteLoading:!1}},created:function(){this.syncShare(),this.syncSharePolling=Object(C["setInterval"])(this.syncShare,12e4)},destroyed:function(){Object(C["clearInterval"])(this.syncSharePolling)},methods:{onCopySuccess:function(){this.$message.success(this.$t("msg.files.linkCopied"))},onCopyPasswordSuccess:function(){this.$message.success(this.$t("msg.files.passwordCopied"))},syncShare:function(){var e=this;this.countdown&&Object(C["clearInterval"])(this.countdown),v.a.get("/api/shares/".concat(this.share.id)).then(function(t){e.share.ttl=t.data.ttl,e.share.files=t.data.files,e.share.ttl&&(e.countdown=Object(C["setInterval"])(e.refreshTtl,1e4))}).catch(function(t){404==t.response.data.status?e.onRemoved(e.share):e.$message.error(t.response.data.message?t.response.data.message:t.toString())})},refreshTtl:function(){this.share.ttl-=1e4},deleteShare:function(){var e=this;this.deleteLoading=!0,this.$confirm(this.$t("msg.files.confirmDelete"),{type:"warning",confirmButtonText:this.$t("msg.files.okButton"),cancelButtonText:this.$t("msg.files.cancelButton")}).then(function(){v.a.delete("/api/shares/".concat(e.share.id),{headers:{token:e.share.token}}).then(function(){e.onRemoved(e.share)}).catch(function(t){e.$message.error(t.response.data.message?t.response.data.message:t.toString())}).finally(function(){e.deleteLoading=!1})}).catch(function(){e.deleteLoading=!1})},humanReadableDataSize:function(e){var t;return t=e>1099511627776?(e/1099511627776).toFixed(1)+"TB":e>1073741824?(e/1073741824).toFixed(1)+"GB":e>1048576?(e/1048576).toFixed(1)+"MB":e>1024?(e/1024).toFixed(1)+"KB":e.toFixed(0)+"Bytes",t}}},P=L,z=Object(l["a"])(P,y,w,!1,null,null,null),F=z.exports,B={components:{files:F,humanReadableDuration:$},data:function(){return{isChooseUpload:!1,share:null,uploadPercentage:0,uploadStatus:null,speed:null,lastLoaded:0,lastLoadTime:null,config:null,ttl:null,uploadedList:null,fileList:null,passwordEnabled:!1,password:null,showPassword:!1,errorMsg:null}},computed:{address:function(){return window.location.href+this.share.id}},created:function(){var e=Object(f["a"])(regeneratorRuntime.mark(function e(){var t,a,s=this;return regeneratorRuntime.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return t=this.$loading(),e.prev=1,e.delegateYield(regeneratorRuntime.mark(function e(){var t,a,i,n,o,r,l,d;return regeneratorRuntime.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return e.next=2,v.a.get("/api/config").then(function(e){s.config=e.data;var t=!0,a=!1,i=void 0;try{for(var n,o=s.config.share.ttlOptions[Symbol.iterator]();!(t=(n=o.next()).done);t=!0){var r=n.value;r.defaultOption&&(s.ttl=r.value)}}catch(l){a=!0,i=l}finally{try{t||null==o.return||o.return()}finally{if(a)throw i}}s.ttl||(s.ttl=s.config.share.ttlOptions[0].value)}).catch(function(e){s.errorMsg=e.response.data.message||e});case 2:if(!s.errorMsg){e.next=4;break}return e.abrupt("return",{v:void 0});case 4:t=null==localStorage.getItem("uploaded")?[]:JSON.parse(localStorage.getItem("uploaded")),a=[],i=!0,n=!1,o=void 0,e.prev=9,r=regeneratorRuntime.mark(function e(){var t;return regeneratorRuntime.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return t=d.value,e.next=3,v.a.post("/api/shares/authorize",t).then(function(){a.push(t)});case 3:case"end":return e.stop()}},e)}),l=t[Symbol.iterator]();case 12:if(i=(d=l.next()).done){e.next=17;break}return e.delegateYield(r(),"t0",14);case 14:i=!0,e.next=12;break;case 17:e.next=23;break;case 19:e.prev=19,e.t1=e["catch"](9),n=!0,o=e.t1;case 23:e.prev=23,e.prev=24,i||null==l.return||l.return();case 26:if(e.prev=26,!n){e.next=29;break}throw o;case 29:return e.finish(26);case 30:return e.finish(23);case 31:if(s.uploadedList=a,s.save(),!s.errorMsg){e.next=35;break}return e.abrupt("return",{v:void 0});case 35:case"end":return e.stop()}},e,null,[[9,19,23,31],[24,,26,30]])})(),"t0",3);case 3:if(a=e.t0,"object"!==Object(g["a"])(a)){e.next=6;break}return e.abrupt("return",a.v);case 6:return e.prev=6,t.close(),e.finish(6);case 9:case"end":return e.stop()}},e,this,[[1,,6,9]])}));function t(){return e.apply(this,arguments)}return t}(),methods:{submitUpload:function(){var e=this,t=new FormData;t.append("ttl",this.ttl),this.passwordEnabled&&this.password&&t.append("password",this.password);var a=0,s=!0,i=!1,n=void 0;try{for(var o,r=this.fileList[Symbol.iterator]();!(s=(o=r.next()).done);s=!0){var l=o.value;a+=l.size,t.append("file",l.raw)}}catch(c){i=!0,n=c}finally{try{s||null==r.return||r.return()}finally{if(i)throw n}}var d=this.util.parseDataSize(this.config.share.maxFileSize);a<d?(v.a.post("/api/shares",t,{onUploadProgress:this.showProgress}).then(function(t){e.handleSuccess(t.data)}).catch(function(t){e.handleError(t)}),this.isChooseUpload=!0):this.$message.error(this.$t("msg.message.fileTooBig",{size:this.config.share.maxFileSize}))},onChange:function(e,t){this.fileList=t},handleSuccess:function(e){this.$message.success(this.$t("msg.message.uploadSuccess")),this.isSuccess=!0,this.share=e,this.uploadStatus="success",this.speed=null,this.uploadedList.unshift(this.share),this.save()},handleError:function(e){this.$message.error(this.$t("msg.message.uploadFail",{errMsg:e.response.data.message||e})),this.errorMsg=e.response.data.message||e,this.uploadStatus="exception"},showProgress:function(e){this.uploadPercentage=parseInt(100*e.loaded/e.total);var t=(new Date).getTime(),a=(t-this.lastLoadTime)/1e3;if(a>1){var s=e.loaded-this.lastLoaded;this.lastLoaded=e.loaded,this.lastLoadTime=t;var i=s/a;this.speed=i>1073741824?(i/1073741824).toFixed(1)+" GB/s":i>1048576?(i/1048576).toFixed(1)+" MB/s":i>1024?(i/1024).toFixed(1)+" KB/s":i.toFixed(0)+" B/s"}},onCopySuccess:function(){this.$message.success(this.$t("msg.message.linkCopied"))},backToHome:function(){this.isChooseUpload=!1,this.uploadStatus=null,this.uploadPercentage=0,this.share=null,this.fileList=null,this.passwordEnabled=!1,this.password=null},shareRemoved:function(e){for(var t=-1,a=0;a<this.uploadedList.length;a++){var s=this.uploadedList[a];if(s.id==e.id){t=a;break}}t>-1&&(this.uploadedList.splice(t,1),this.save())},save:function(){var e=JSON.parse(JSON.stringify(this.uploadedList)),t=!0,a=!1,s=void 0;try{for(var i,n=e[Symbol.iterator]();!(t=(i=n.next()).done);t=!0){var o=i.value;o.ttl=null}}catch(r){a=!0,s=r}finally{try{t||null==n.return||n.return()}finally{if(a)throw s}}localStorage.setItem("uploaded",JSON.stringify(e))}}},O=B,T=(a("41f3"),Object(l["a"])(O,u,h,!1,null,null,null)),U=T.exports,M=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("el-card",{staticStyle:{width:"560px","min-height":"400px",display:"inline-block",margin:"10px"}},[a("div",{staticStyle:{"min-height":"344px"}},[e.share?a("div",{staticStyle:{height:"310px"}},[a("span",[e._v(e._s(e.$t("msg.download.title")))]),a("el-divider"),a("files",{staticStyle:{margin:"0px 50px 0px 50px"},attrs:{share:e.share,"on-removed":e.shareRemoved}})],1):e.needPassword?a("div",{staticStyle:{height:"310px"}},[a("el-row",{staticStyle:{height:"70px"}}),a("el-row",[a("span",[e._v(e._s(e.$t("msg.download.needPassword")))])]),a("el-row",{staticStyle:{"margin-top":"10px"}},[a("el-input",{staticStyle:{width:"300px","margin-right":"10px"},attrs:{autofocus:"",type:e.showPassword?"text":"password"},nativeOn:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.authorize(t)}},model:{value:e.password,callback:function(t){e.password=t},expression:"password"}},[a("i",{class:"el-input__icon iconfont "+(e.showPassword?"icon-eye-open":"icon-eye-close"),staticStyle:{cursor:"pointer"},attrs:{slot:"suffix"},on:{click:function(t){e.showPassword=!e.showPassword}},slot:"suffix"})]),a("el-button",{attrs:{type:"primary"},on:{click:e.authorize}},[e._v(e._s(e.$t("msg.download.unlockButton")))])],1)],1):a("div",{staticStyle:{height:"310px",width:"100%",display:"table","text-align":"center"}},[a("p",{staticStyle:{"font-size":"large","vertical-align":"middle",display:"table-cell"}},[e._v(e._s(e.errorMsg))])]),a("div",{staticStyle:{"margin-top":"20px",width:"100%"}},[a("el-link",{attrs:{type:"primary"},on:{click:e.gotoUpload}},[e._v(e._s(e.$t("msg.download.gotoUploadLink")))])],1)])])],1)},E=[],j={components:{files:F},data:function(){return{share:null,errorMsg:null,needPassword:!1,password:null,showPassword:!1}},created:function(){this.getShare()},methods:{gotoUpload:function(){this.$router.push("/")},authorize:function(){var e=this;v.a.post("/api/shares/authorize",{id:this.$route.params.shareId,password:this.password}).then(function(){e.getShare()}).catch(function(t){401==t.response.data.status?e.$message.error(e.$t("msg.download.wrongPassword")):e.$message.error(t.response.data.message?t.response.data.message:t)})},getShare:function(){var e=this,t=this.$loading();v.a.get("/api/shares/".concat(this.$route.params.shareId),{}).then(function(t){e.share=t.data,e.needPassword=!1}).catch(function(t){401==t.response.data.status?e.needPassword=!0:e.errorMsg=t.response.data.message||t}).finally(function(){t.close()})},shareRemoved:function(){this.errorMsg=this.$t("msg.download.fileExpired"),this.needPassword=!1,this.share=null}}},D=j,I=Object(l["a"])(D,M,E,!1,null,null,null),R=I.exports;s["default"].use(p["a"]);var N=new p["a"]({routes:[{path:"/",component:U},{path:"/:shareId",component:R}]}),H=a("4eb5"),W=a.n(H),J=a("a925"),K=a("5c96"),G=a.n(K),Y=(a("0fae"),a("aef6"),{parseDataSize:function(e){var t;return e.endsWith("TB")?t=1024*parseInt(e.slice(0,e.length-2).trim())*1024*1024*1024:e.endsWith("GB")?t=1024*parseInt(e.slice(0,e.length-2).trim())*1024*1024:e.endsWith("MB")?t=1024*parseInt(e.slice(0,e.length-2).trim())*1024:e.endsWith("KB")?t=1024*parseInt(e.slice(0,e.length-2).trim()):e.endsWith("B")&&(t=parseInt(e.slice(0,e.length-1).trim())),t}});a("ed2c");s["default"].use(J["a"]),s["default"].config.productionTip=!1,s["default"].use(G.a),s["default"].use(W.a),s["default"].prototype.util=Y;var A=new J["a"]({locale:navigator.language||navigator.userLanguage,messages:{"zh-CN":a("9df6"),"en-US":a("3fab")}});new s["default"]({router:N,render:function(e){return e(c)},i18n:A}).$mount("#app")},"64a9":function(e,t,a){},"9df6":function(e,t,a){"use strict";a.r(t),a.d(t,"msg",function(){return s});var s={languageName:"中文",upload:{dragFile:{1:"将文件拖到此处，或",2:"点击添加文件"},tip:{normal:"可上传{size}以内的文件",anySize:"可上传任意大小的文件"},expire:"后过期",enablePassword:"密码保护",uploadButton:"上传",wrongToken:"Token验证错误"},uploaded:{downloadLink:"下载链接：",copyLinkButton:"复制链接",tip:"复制链接，粘贴到浏览器地址栏中，并打开页面",backToHomeLink:"再次上传"},message:{uploading:"正在上传，成功后生成下载链接。。。",uploadSuccess:"上传成功",uploadFail:"上传失败。{errMsg}",fileTooBig:"文件过大，不能超过{size}",linkCopied:"链接已复制"},expireTip:{normal:"文件将于{ttl}后过期",neverExpire:"文件永不过期"},durationUnit:{day:"天",hour:"小时",minute:"分钟",D:"天",H:"小时",M:"分钟"},download:{title:"文件（点击文件名下载）",needPassword:"文件已加密，请输入密码",unlockButton:"解锁",gotoUploadLink:"使用SendFile共享文件",fileExpired:"文件已过期",wrongPassword:"密码错误"},files:{copyLink:"复制链接",copyPassword:"复制密码",deleteFile:"删除文件",linkCopied:"链接已复制",passwordCopied:"密码已复制",confirmDelete:"确定删除文件共享？",okButton:"确定",cancelButton:"取消"}}},bd16:function(e,t,a){},ed2c:function(e,t,a){}});
//# sourceMappingURL=app.4800c99c.js.map