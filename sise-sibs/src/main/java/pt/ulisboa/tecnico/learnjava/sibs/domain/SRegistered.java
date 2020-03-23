package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;

public class SRegistered implements TransferOperationState {
	TransferOperation transferOperation;

	public SRegistered(TransferOperation newTransferOperation) {
		this.transferOperation = newTransferOperation;

	}

	@Override
	public void process() {

		try {
			this.transferOperation.getService().withdraw(this.transferOperation.getSourceIban(),
					this.transferOperation.getValue());
		} catch (AccountException e) {
			System.out.println("Something went wrong with the SRegisted");
			this.transferOperation.setTransferOperationState(this.transferOperation.getErrorState());

		}

		this.transferOperation.setTransferOperationState(this.transferOperation.getWithdrawnState());

	}

	@Override
	public void cancel() {
		this.transferOperation.setTransferOperationState(this.transferOperation.getCanceledState());

	}

}
