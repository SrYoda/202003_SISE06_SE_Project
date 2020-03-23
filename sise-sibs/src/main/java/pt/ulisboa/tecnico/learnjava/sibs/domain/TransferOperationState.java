package pt.ulisboa.tecnico.learnjava.sibs.domain;

public interface TransferOperationState {
	void process();

	void cancel();
}
