package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;

public class SWithdrawn implements TransferOperationState {

	TransferOperation transferOperation;

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
			} catch (AccountException e) {
				System.out.println("Something went wrong with the SWithdrawn Same Bank");
				this.transferOperation.setTransferOperationState(this.transferOperation.getErrorState());

			}
			this.transferOperation.setTransferOperationState(this.transferOperation.getCompletedState());
		}

		else {
			try {
				this.transferOperation.getService().deposit(this.transferOperation.getTargetIban(),
						this.transferOperation.getValue());
			} catch (AccountException e) {
				System.out.println("Something went wrong with the SWithdrawn Different Bank");
				this.transferOperation.setTransferOperationState(this.transferOperation.getErrorState());

			}
			this.transferOperation.setTransferOperationState(this.transferOperation.getDepositedState());
		}

	}

	@Override
	public void cancel() {
		try {
			this.transferOperation.getService().deposit(this.transferOperation.getSourceIban(),
					this.transferOperation.getValue());
		} catch (AccountException e) {
			System.out.println("Something went wrong with the SWithdrawn Canceled Operation");
			this.transferOperation.setTransferOperationState(this.transferOperation.getErrorState());

		}

		this.transferOperation.setTransferOperationState(this.transferOperation.getCanceledState());

	}
}