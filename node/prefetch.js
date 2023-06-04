const app = require('./app');
const hash = require('./structures/hash');
const fs = require('fs');

const bptree = app.bptreeInstance;
const cache = app.cacheInstance;

function getLinkedArticles(content) {
	linked = content.match(/(?<=\[\[).+?(?=\]\]|\|.*\]\]|#.*\]\])/g);

	countMap = new Map();
	for (article of linked) {
		count = countMap.has(article) ? countMap.get(article)+1 : 1;
		countMap.set(article, count);
	}
	
	result = Array.from(countMap, ([title, count], index)=>({ title, count, index }));

	const stableSort = (a, b) => {
  		if (a.count === b.count) return a.index - b.index;
  		else if (a.count > b.count) return -1;
  		else return 1;
	};
	result.sort(stableSort);

	return(result.slice(0, 5));
}

function prefetch(content, dir, fileName, filePath) {
	articleList = getLinkedArticles(content);

	for (article of articleList) {
		let hashedTitle = hash.hashStringTo8ByteInt(article.title);
		if (!cache.has(hashedTitle)) {
			let value = bptree.get(hashedTitle);
			if (!value) continue; // Not in dump file

			let fileNum = value.fileNum;
			let readArticle = '';
        	const stream = fs.createReadStream(filePath(), {
          		encoding: 'utf8',
          		start: value.start,
          		end: value.end
        	});
        	stream
          		.on('data', (data) => {
            		readArticle += data;
          		})
          		.on('end', () => {
            		cache.insert(hashedTitle, readArticle, value.end - value.start);
            		// console.log(`Prefetched: ${article.title} / cache size: ${cache.size}`);
          		});
		}
	}
}

module.exports = { prefetch };