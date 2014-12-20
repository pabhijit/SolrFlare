var Manager;

(function ($) {

	$(function () {
		Manager = new AjaxSolr.Manager({
			solrUrl: 'http://localhost:8983/solr/rss/'
		});
		var theUrl = "http://localhost:8983/solr/rss/dataimport?command=full-import&clean=false";
		var xmlHttp = null;
		xmlHttp = new XMLHttpRequest();
		xmlHttp.open( "GET", theUrl, false );
		Manager.addWidget(new AjaxSolr.ResultWidget({
			id: 'result',
			target: '#docs'
		}));
		Manager.addWidget(new AjaxSolr.PagerWidget({
			id: 'pager',
			target: '#pager',
			prevLabel: '&lt;',
			nextLabel: '&gt;',
			innerWindow: 1,
			renderHeader: function (perPage, offset, total) {
				$('#pager-header').html($('<span></span>').text('displaying ' + Math.min(total, offset + 1) + ' to ' + Math.min(total, offset + perPage) + ' of ' + total));
			}
		}));
		
		Manager.addWidget(new AjaxSolr.AutocompleteWidget({
			id: 'text',
			target: '#search',
			//fields: [ 'topics', 'organisations', 'exchanges' ] %change
			fields: [ 'category' ]
		}));
		Manager.addWidget(new AjaxSolr.CalendarWidget({
			id: 'calendar',
			target: '#calendar',
			field: 'date'
		}));
		Manager.init();
		Manager.store.addByValue('q', '*:*');
		var params = {
				facet: true,
				//'facet.field': [ 'topics', 'organisations', 'exchanges', 'countryCodes' ], %change
				'facet.field': [ 'category' ],
				'facet.limit': 20,
				'facet.mincount': 1,
				///'f.topics.facet.limit': 50, %change
				'facet.category.limit': 50,
				//'f.countryCodes.facet.limit': -1,%change
				//

				//'facet.date': 'date',
				//'facet.date.start': '1987-02-26T00:00:00.000Z/DAY',
				//'facet.date.end': '1987-10-20T00:00:00.000Z/DAY+1DAY',
				//'facet.date.gap': '+1DAY',
				//'json.nl': 'map'
		};
		for (var name in params) {
			Manager.store.addByValue(name, params[name]);
		}
		$.get("/SolrFlare/SemanticAnalysis", function(result) {
			var pref = result.trim().split(" ");
			var pref1 = pref[0];
			var pref2 = pref[1];
			var pref3 = pref[2];
			var params = {
					defType: 'edismax',
					bq:[pref1+"^30"+pref2+"^40"+pref3+"^80"]
			};
			for (var name in params){
				Manager.store.addByValue(name, params[name]);
			};
			Manager.doRequest();
		});
	});
	$.fn.showIf = function (condition) {
		if (condition) {
			return this.show();
		}
		else {
			return this.hide();
		}
	}
})(jQuery);
