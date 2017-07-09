var sum,m,n
begin
    read(m);
    read(n);
    while n#0 do
        begin
            sum:=sum+m;
            m:=m+1;
        end
    write(sum);
end.