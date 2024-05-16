package net.fhirfactory.pegacorn.communicate.matrix.methods.common;

import net.fhirfactory.pegacorn.communicate.common.SimpleHTTPQuery;

public class MatrixQuery extends SimpleHTTPQuery {

    //
    // Constructor(s)
    //

    public MatrixQuery() {
        super();
    }

    //
    // TO String
    //

    @Override
    public String toString() {
        return "MatrixQuery{" +
                "httpMethod='" + getHttpMethod() + '\'' +
                ", httpPath='" + getHttpPath() + '\'' +
                ", body='" + getBody() + '\'' +
                ", httpParameters=" + getHttpParameters() +
                '}';
    }
}
