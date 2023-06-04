const { ZipfGenerator } = require('./zipf.js');
const { ArgumentParser } = require('argparse');
var fs = require('fs');
D = 27379; // number of documents
R = D; // number of requests
const parser = new ArgumentParser({});
parser.add_argument('-a', '--address');
address = parser.address
const zipfGen = new ZipfGenerator(D); 

const cntArr = Array.from({ length: D }, (_, index) => 0);
for(let i=0; i<R; i++){ 
    cntArr[zipfGen.nextInt()]++;
}

console.log(cntArr)

fs.open('test/zipf_url.txt','w',function(err,fd){
	if (err) throw err;
 	console.log('file open complete');
});

var urls = fs.readFileSync('test/url.txt','utf8').split("\n")
console.log(urls[0])

for(let i=0; i<D; i++){
    var query = urls[i] + '\n'
    console.log(cntArr[i])
    for(let j=0;j<cntArr[i];j++){
        fs.appendFileSync('test/zipf_url.txt', query, 'utf8', { 'flags': 'a+' })
    }
}