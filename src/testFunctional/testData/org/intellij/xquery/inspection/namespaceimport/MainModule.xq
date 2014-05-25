module namespace mainModule = "my://org/intellij/xquery/inspection/namespaceimport/MainModule.xq";

import module namespace moduleOne = "my://org/intellij/xquery/inspection/namespaceimport/moduleOne.xq";
import module namespace <warning descr="Namespace prefix in module import should be same as in module declaration">moduleThree</warning> = "my://org/intellij/xquery/inspection/namespaceimport/moduleTwo.xq";


declare function moduleOne:mainFunction() {
    moduleOne:someFunction() and moduleThree:someFunction()
};
