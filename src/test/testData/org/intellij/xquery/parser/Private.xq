declare %private variable $private := "private";
declare %private variable $privateItem as item()* := ();
declare %private variable $privateBoolean as xs:boolean := fn:false();


declare function public($private:private) as item()* {
    $private,$private:private
};

declare %private function booleanfun($some) as xs:boolean {
    fn:true()
};

declare %private function private($private:private) {
    $private,$private:private
};

private($private)