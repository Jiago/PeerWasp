package org.peerbox.watchservice;

import java.nio.file.Path;

import org.hive2hive.core.api.interfaces.IFileManager;
import org.hive2hive.core.exceptions.NoPeerConnectionException;
import org.hive2hive.core.exceptions.NoSessionException;
import org.peerbox.model.H2HManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveState implements ActionState {
	
	private final static Logger logger = LoggerFactory.getLogger(MoveState.class);
	
	@Override
	public ActionState handleCreateEvent() {
		logger.debug("Create Request denied: Cannot change from Move to Create.");
		return new MoveState();
	}

	@Override
	public ActionState handleDeleteEvent() {
		logger.debug("Delete Request accepted: State changed from Move to Delete.");
		return new DeleteState();
	}

	@Override
	public ActionState handleModifyEvent() {
		logger.debug("Modify Request denied: Cannot change from Move to Modify State.");
		return new MoveState();
	}
	
	@Override
	public void execute(Path filePath) throws NoSessionException, NoPeerConnectionException {
		logger.debug("Move State: Execute \"Move File\" H2H API call");
		H2HManager manager = new H2HManager();
		IFileManager fileHandler = manager.getNode().getFileManager();
		
		//H2H move needs to be analyzed to make sure how it works
		fileHandler.move(filePath.toFile(),filePath.toFile());
		logger.debug("Task \"Add File\" executed.");		
	}


}
