const { ArgumentParser } = require('argparse');
const fs = require('fs');
const parser = new ArgumentParser({});
parser.add_argument('-t', '--txt_dir');
parser.add_argument('-p', '--pattern');
parser.add_argument('-s', '--stride');

args = parser.parse_args();
var text = fs.readFileSync(args.txt_dir, 'utf8')
var stride = parseInt(args.stride);
console.log(text);
var textList = text.split("\n");
Array.prototype.mySlice = function(b,e,s) {
    var n=[];
    for (var i=b; i<e; i+=s) {
        n.push(this[i]);
    }
    return n;
};

if (args.pattern === "seq") {
    (async() => {

        const tests = async(apiList) => {
            let start = new Date()
            var results = []
            await Promise.all(apiList.map(api => fetch(api)))
                .then(responses => Promise.all(responses.map(r => r.text())))
                .then(result => {
                    result.map((users)=>{
                        console.log(users)
                        results.push(users)
                    })
                    }
                )
            let end = new Date()
            console.log(`Result time: ${end-start}ms, list length: ${apiList.length}`)
            return results
        }
    
        for (let i=0; i<stride; i++) {
            results = await tests(textList.mySlice(i, textList.length, stride))
            // console.log(results)
            // console.log(results.length)
        }
    })()   
}
