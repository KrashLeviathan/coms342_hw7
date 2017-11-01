package funclang;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class represents area in the memory reserved for dynamic memory allocation
 *
 * @author Nathan Karasch (nkarasch@iastate.edu)
 */
public interface Heap {
    Value ref(Value val);

    Value deref(Value.RefVal loc);

    Value setref(Value.RefVal loc, Value val);

    Value free(Value.RefVal loc);

    static class Heap16Bit implements Heap {
        Value[] _rep;
        Queue<Integer> _freeSpaces = new LinkedList<>();
        int _furthestIndex = -1;

        /**
         * The method ref takes a single parameter val, of type Value
         * allocates memory and stores val in allocated memory at location l
         *
         * @param val the value to be stored in the Heap
         * @return a RefVal containing location l
         */
        @Override
        public Value ref(Value val) {
            int loc = addToHeap(val);
            return new Value.RefVal(loc);
        }

        /**
         * The method deref takes a single parameter loc, of type RefVal
         *
         * @param loc the pointer you want to dereference
         * @return value stored at location l, where l is stored in loc.
         */
        @Override
        public Value deref(Value.RefVal loc) {
            return _rep[loc.loc()];
        }

        /**
         * The method setref takes two parameters.
         *
         * @param loc the pointer address for a location on the Heap
         * @param val the value to store at location loc
         * @return the Value val
         */
        @Override
        public Value setref(Value.RefVal loc, Value val) {
            _rep[loc.loc()] = val;
            return val;
        }

        /**
         * The method free takes a single parameter loc, of type RefVal
         * which encapsulates location l.
         * It deallocates the memory location l from rep.
         *
         * @param loc the location to be freed
         * @return the RefVal of the memory location freed
         */
        @Override
        public Value free(Value.RefVal loc) {
            _freeSpaces.add(loc.loc());
            _rep[loc.loc()] = null;
            return loc;
        }

        private void doubleHeapSize() {
            Value[] doubledArray = new Value[_rep.length * 2];
            System.arraycopy(_rep, 0, doubledArray, 0, _rep.length);
            _rep = doubledArray;
        }

        private int addToHeap(Value value) {
            if (!_freeSpaces.isEmpty()) {
                int index = _freeSpaces.poll();
                _rep[index] = value;
                return index;
            }
            _furthestIndex++;
            if (_furthestIndex == _rep.length) {
                doubleHeapSize();
            }
            _rep[_furthestIndex] = value;
            return _furthestIndex;
        }
    }
}
