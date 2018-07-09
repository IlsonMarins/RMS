define(function(){
    return {
        pageGroups: [{"id":"a3891bec-ecf2-d8e2-9f22-f5d52acbd449","name":"Default group","pages":[{"id":"42f22a35-88db-26e7-938e-0dcfc46955f6","name":"Solicitação"},{"id":"aff61bc0-b167-58a1-faaf-82a737479eb6","name":"Agendamentos"}]}],
        downloadLink: "//services.ninjamock.com/html/htmlExport/download?shareCode=5Z2NH&projectName=Gestor de REORGs",
        startupPageId: 0,

        forEachPage: function(func, thisArg){
        	for (var i = 0, l = this.pageGroups.length; i < l; ++i){
                var group = this.pageGroups[i];
                for (var j = 0, k = group.pages.length; j < k; ++j){
                    var page = group.pages[j];
                    if (func.call(thisArg, page) === false){
                    	return;
                    }
                }
            }
        },
        findPageById: function(pageId){
        	var result;
        	this.forEachPage(function(page){
        		if (page.id === pageId){
        			result = page;
        			return false;
        		}
        	});
        	return result;
        }
    }
});
