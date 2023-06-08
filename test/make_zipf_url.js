const { ZipfGenerator } = require('./zipf.js');
const { ArgumentParser } = require('argparse');
var fs = require('fs');

const parser = new ArgumentParser({});
parser.add_argument('-a', '--address');
parser.add_argument('-n', '--number');
parser.add_argument('-d', '--document');
parsed_args = parser.parse_args()
D = parsed_args.document; // number of documents
R = 3*D; // number of requests
address = parsed_args.address
file_num = parsed_args.number
console.log(address)
console.log(file_num)
const zipfGen = new ZipfGenerator(D); 

const cntArr = Array.from({ length: D }, (_, index) => 0);
for(let i=0; i<R; i++){ 
    cntArr[zipfGen.nextInt()]++;
}

console.log(cntArr)

fs.open('test/zipf_urls/url_'+file_num+'.txt','w',function(err,fd){
	if (err) throw err;
 	console.log('file open complete');
});

var urls = fs.readFileSync('test/urls/url_'+file_num+'.txt','utf8').split("\n")
console.log(urls[0])

for(let i=0; i<D; i++){
    var query = urls[i] + '\n'
    console.log(cntArr[i])
    for(let j=0;j<cntArr[i];j++){
        fs.appendFileSync('test/zipf_urls/url_'+file_num+'.txt', query, 'utf8', { 'flags': 'a+' })
    }
}