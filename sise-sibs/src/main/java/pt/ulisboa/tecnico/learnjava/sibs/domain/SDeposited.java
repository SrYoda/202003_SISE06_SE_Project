package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;

public class SDeposited implements TransferOperationState {

	TransferOperation transferOperation;

	public SDeposited(TransferOperation newTransferOperation) {
		this.transferOperation = newTransferOperation;

	}

	@Override
	public void process() {
		try {
			this.transferOperation.getService().withdraw(this.transferOperation.getSourceIban(),
					this.transferOperation.commission());
		} catch (AccountException e) {
			System.out.println("Something went wrong with the SDeposited");
			this.transferOperation.setTransferOperationState(this.transferOperation.getErrorState());

		}

		this.transferOperation.setTransferOperationState(this.transferOperation.getCompletedState());
	}

	@Override
	public void cancel() {
		try {
			this.transferOperation.getService().deposit(this.transferOperation.getSourceIban(),
					this.transferOperation.getValue());
			this.transferOperation.getService().withdraw(this.transferOperation.getTargetIban(),
					this.transferOperation.getValue());

		} catch (AccountException e) {
			System.out.println("Something went wrong with the SDeposited Canceled Operation");
			this.transferOperation.setTransferOperationState(this.transferOperation.getErrorState());

		}

		this.transferOperation.setTransferOperationState(this.transferOperation.getCanceledState());

	}

}
