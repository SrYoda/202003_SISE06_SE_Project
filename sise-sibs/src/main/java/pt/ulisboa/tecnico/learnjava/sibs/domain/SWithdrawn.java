package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ServicesException;

public class SWithdrawn implements TransferOperationState {

	TransferOperation transferOperation;
	int counter = 0;

	public SWithdrawn(TransferOperation newTransferOperation) {
		this.transferOperation = newTransferOperation;

	}

	@Override
	public void process() {

		if ((this.transferOperation.getTargetIban().substring(0, 3)
				.equals(this.transferOperation.getSourceIban().substring(0, 3)))) {
			try {
				this.transferOperation.getService().deposit(this.transferOperation.getTargetIban(),
						this.transferOperation.getValue());
				this.transferOperation.setTransferOperationState(this.transferOperation.getCompletedState());
			} catch (AccountException | ServicesException e) {
				if (this.counter == 2) {
					this.transferOperation.setTransferOperationState(this.transferOperation.getErrorState());
				} else {
					this.counter += 1;
				}
				System.out.println("Something went wrong with the SWithdrawn Same Bank");
			}

		}

		else {
			try {
				this.transferOperation.getService().deposit(this.transferOperation.getTargetIban(),
						this.transferOperation.getValue());
				this.transferOperation.setTransferOperationState(this.transferOperation.getDepositedState());
			} catch (AccountException | ServicesException e) {
				if (this.counter == 2) {
					this.transferOperation.setTransferOperationState(this.transferOperation.getErrorState());
				} else {
					this.counter += 1;
				}
				System.out.println("Something went wrong with the SWithdrawn Same Bank");

			}

		}

	}

	@Override
	public void cancel() {
		try {
			this.transferOperation.getService().deposit(this.transferOperation.getSourceIban(),
					this.transferOperation.getValue());
			this.transferOperation.setTransferOperationState(this.transferOperation.getCanceledState());
		} catch (AccountException | ServicesException e) {
			if (this.counter == 2) {
				this.transferOperation.setTransferOperationState(this.transferOperation.getErrorState());
			} else {
				this.counter += 1;
			}
			System.out.println("Something went wrong with the SWithdrawn Canceled Operation");

		}

	}
}