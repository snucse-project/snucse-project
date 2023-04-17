const express = require('express');
const Siteinfo = require('../schemas/siteinfo');

const router = express.Router();

// GET siteinfo by siteinfo_id
router.get('/siteinfo/:siteinfoid', async (req, res, next) => {
  try{
    const siteinfo = await Siteinfo.findById(req.params.siteinfoid);
    console.log(siteinfo);
    res.json(siteinfo);
  } catch (err) {
    console.error(err);
    next(err);
  }
});

module.exports = router;