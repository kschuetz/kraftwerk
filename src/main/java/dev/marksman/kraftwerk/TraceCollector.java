package dev.marksman.kraftwerk;

import java.util.ArrayList;

public class TraceCollector<State> {
    public ArrayList<Trace<?>> traces;
    public State state;

    public TraceCollector(State state) {
        this.traces = new ArrayList<>();
        this.state = state;
    }
}
