/**
 * Zipf Distribution Generator.
 * Logic originated from YCSB
 * @see https://github.com/brianfrankcooper/YCSB
 * As noted there,
 * The algorithm used here is from "Quickly Generating Billion-Record Synthetic
 * Databases", Jim Gray et al, SIGMOD 1994.
 * JS implementation reference: @see https://github.com/willscott/zipfian/
 */

class ZipfGenerator {
    constructor(items, theta, zetan) {
        this.items = items;
        this.base = 0;
        this.theta = 0.99;
        this.zetan = this.getZeta(0, this.items, this.theta, 0);
        this.eta = (1 - Math.pow(2.0 / this.items, 1 - this.theta)) /
            (1 - this.getZeta(0, 2, this.theta, 0) / this.zetan);
        this.randArr = this.shuffleArray(items);
    }

    getZeta(st, n, theta, initsum) {
        let sum = initsum;
        for (let i = st; i < n; i++) {
            sum += 1.0 / Math.pow(i + 1, theta);
        }
        return sum;
    }

    shuffleArray(n) {
        let arr = Array.from({ length: n }, (_, index) => index);
        for (let i = n - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [arr[i], arr[j]] = [arr[j], arr[i]];
        }
        return arr;
    }

    nextInt(){
        const u = Math.random();
        const uz = u * this.zetan;
        if(uz < 1.0)
            return this.randArr[0];
        else
            return this.randArr[Math.floor(this.items *
                Math.pow(this.eta * u - this.eta + 1, 1.0 / (1.0 - this.theta)))];
    }
}

module.exports = {ZipfGenerator};