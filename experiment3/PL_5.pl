var m,n,r,q,m0,n0,result;
procedure gcd;
    begin
        while r #0 do
            begin
                q:=m/n;
                r:=m-q*n;
                m:=n;
                n:=r;
            end
    end;
procedure lcs;
    begin
        call gcd;
        result=m0*n0/m;
    end;

begin
    read(m);
    read(n);
    m0:=m;
    n0:=n;
    if m<n then
        begin
            r:=m;
            m:=n;
            n:=r;
        end;
     begin
        r:=1;
        call lcs;
        write(result);
     end;
 end.