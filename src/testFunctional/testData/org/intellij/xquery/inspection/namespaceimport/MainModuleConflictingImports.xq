module namespace mainModuleConflictingImports = "my://src/MainModuleConflictingImports.xq";

import module namespace moduleOne = "moduleOne.xq";
import module namespace moduleThree = "a/moduleThree.xq";
import module namespace moduleThreeOther = "b/moduleThree.xq";


declare function mainModuleConflictingImports:mainFunction() {
    moduleOne:someFunction() and moduleThree:someFunction() and moduleThreeOther:someFunction()
};
