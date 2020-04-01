package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ServicesException;

public class SDeposited implements TransferOperationState {

	TransferOperation transferOperation;
	int counter = 0;

	public SDeposited(TransferOperation newTransferOperation) {
		this.transferOperation = newTransferOperation;

	}

	@Override
	public void process() {
		try {
			this.transferOperation.getService().withdraw(this.transferOperation.getSourceIban(),
					this.transferOperation.commission());
			this.transferOperation.setTransferOperationState(this.transferOperation.getCompletedState());
		} catch (AccountException | ServicesException e) {
			if (this.counter == 2) {

				this.transferOperation.setTransferOperationState(this.transferOperation.getErrorState());

			} else {
				this.counter += 1;
			}
			System.out.println("Something went wrong with the SDeposited");
		}
	}

	@Override
	public void cancel() {
		try {
			this.transferOperation.getService().deposit(this.transferOperation.getSourceIban(),
					this.transferOperation.getValue());
			this.transferOperation.getService().withdraw(this.transferOperation.getTargetIban(),
					this.transferOperation.getValue());
			this.transferOperation.setTransferOperationState(this.transferOperation.getCanceledState());
		} catch (AccountException | ServicesException e) {
			if (this.counter == 2) {
				this.transferOperation.setTransferOperationState(this.transferOperation.getErrorState());
			} else {
				this.counter += 1;
			}
			System.out.println("Something went wrong with the SDeposited Canceled Operation");

		}

	}

}
