package com.hwant.services;

import android.os.AsyncTask;

public class IMAsyncTask extends AsyncTask<Void, Void, Object> {
	private IDoWork work = null;

	public IMAsyncTask(IDoWork work) {
		this.work = work;
	}

	@Override
	protected Object doInBackground(Void... params) {
		Object obj = null;
		if (!isCancelled()&&work != null) {
			try {
				obj=work.doWhat();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return obj;
	}

	@Override
	protected void onPostExecute(Object result) {
		if (work != null) {
			work.Finish2Do(result);
		}
	}

}
