module namespace mainModule = "my://src/MainModule.xq";

import module namespace moduleOne = "moduleOne.xq";
import module namespace <warning descr="Namespace prefix in module import should be same as in module declaration">moduleThree</warning> = "src/moduleTwo.xq";


declare function moduleOne:mainFunction() {
    moduleOne:someFunction() and moduleThree:someFunction()
};
