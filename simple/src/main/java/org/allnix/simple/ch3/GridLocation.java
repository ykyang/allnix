package org.allnix.simple.ch3;

/**
 * CSP: 3.4
 * 
 * @author ykyang@gmail.com
 *
 */
public class GridLocation {
    public final int row, column;

    public GridLocation(int row, int column) {
        super();
        this.row = row;
        this.column = column;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + column;
        result = prime * result + row;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GridLocation other = (GridLocation) obj;
        if (column != other.column)
            return false;
        if (row != other.row)
            return false;
        return true;
    }

    
}
