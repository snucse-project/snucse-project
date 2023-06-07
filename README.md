# snucse-project
Creative Integrated Design project repository (with MOLOCO)
<br/><br/>

## Guideline for dump files
* Every file must be in the same directory.
* The dump file name must be in the form of _(fileName)\_(number).json_, where every file has the same _fileName_.
* Every dump file must have a corresponding parsed file which is created by _parse.py_. The name of the file should be in the form of: _(dumpFileName)\_parsed.json_
* Send a POST request with its body including the address of the directory that contains dump files.