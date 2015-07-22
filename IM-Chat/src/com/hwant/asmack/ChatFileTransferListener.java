package com.hwant.asmack;

import java.io.File;
import java.io.IOException;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;

import android.os.Environment;
import android.util.Log;

import com.hwant.common.Common;

public class ChatFileTransferListener implements FileTransferListener {

	@Override
	public void fileTransferRequest(final FileTransferRequest request) {
		new Thread() {
			@Override
			public void run() {

				try {
					File file = new File(
							Environment.getExternalStorageDirectory()
									+ Common.Path_Media + request.getFileName());
					if (!file.exists()) {
						Log.i("have no file", file.getPath());
						try {
							file.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					IncomingFileTransfer transfer = request.accept();
					transfer.recieveFile(file);
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

}
