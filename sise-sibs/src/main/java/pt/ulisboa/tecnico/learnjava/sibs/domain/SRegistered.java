package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ServicesException;

public class SRegistered implements TransferOperationState {
	TransferOperation transferOperation;
	int counter = 0;

	public SRegistered(TransferOperation newTransferOperation) {
		this.transferOperation = newTransferOperation;

	}

	@Override
	public void process() {

		try {
			this.transferOperation.getService().withdraw(this.transferOperation.getSourceIban(),
					this.transferOperation.getValue());
			this.transferOperation.setTransferOperationState(this.transferOperation.getWithdrawnState());
		} catch (AccountException | ServicesException e) {
			if (this.counter == 2) {
				this.transferOperation.setTransferOperationState(this.transferOperation.getErrorState());
			} else {
				this.counter += 1;
			}
			System.out.println("Something went wrong with the SRegistration");

		}
	}

	@Override
	public void cancel() {
		this.transferOperation.setTransferOperationState(this.transferOperation.getCanceledState());

	}

}
