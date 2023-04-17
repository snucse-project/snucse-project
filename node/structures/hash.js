const crypto = require('crypto');

function hashStringTo8ByteInt(str) {
  const hash = crypto.createHash('sha256').update(str).digest();
  const truncatedHash = hash.subarray(0, 8);
  const num = truncatedHash.readBigInt64BE();
  return Number(num);
}

module.exports = {hashStringTo8ByteInt};