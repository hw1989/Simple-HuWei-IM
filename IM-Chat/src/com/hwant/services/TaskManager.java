package com.hwant.services;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.os.AsyncTask.Status;

import com.hwant.common.Common;

public class TaskManager {
	private static TaskManager manager = null;
	private static ExecutorService threadpool = null;
	private ArrayList<Combination> list = null;
	private final static int CPU = Runtime.getRuntime().availableProcessors();
	private int minsize = CPU + 1;
	private int maxsize = CPU * 2 + 1;

	private TaskManager() {
		threadpool = Executors.newFixedThreadPool(Common.XMPP_Thread_Pool_Size);
		threadpool = new ThreadPoolExecutor(minsize, maxsize, 1,
				TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(128));
		list = new ArrayList<Combination>();

	}

	public static TaskManager init() {
		if (manager == null) {
			manager = new TaskManager();
		}
		return manager;
	}

	/**
	 * 添加带有标记的任务，方便清空同一类的标记
	 * 
	 * @param flag
	 *            工作任务的标记
	 * @param work
	 *            工作任务
	 */
	public void addTask(String flag, IDoWork work) {
		IMAsyncTask task = new IMAsyncTask(work);
		Combination combination = new Combination();
		combination.flag = flag;
		combination.weak = new WeakReference<IMAsyncTask>(task);
		list.add(combination);
		task.executeOnExecutor(threadpool);
	}

	public void addTask(IDoWork work) {
		addTask("", work);
	}

	// 清空任务池
	public void removeAll() {
		for (Combination combination : list) {
			if (combination.weak != null) {
				if (combination.weak.get() != null) {
					combination.weak.get().cancel(true);
				}
			}
		}
		list.clear();
	}

	/**
	 * 区分大小写
	 */
	public void remove(String str) {
		Combination combination = null;
		IMAsyncTask task = null;
		Iterator<Combination> iterator = list.iterator();
		while (iterator.hasNext()) {
			combination = iterator.next();
			task = combination.weak.get();
			if (str.equals(combination.flag)) {
				if (task != null) {
					// 取消异步任务
					task.cancel(true);
				}
				list.remove(combination);
			} else {
				if (task != null) {
					// 异步任务结束
					if (task.getStatus() == Status.FINISHED) {
						list.remove(combination);
					}
				}
			}
		}
	}

	// 退出线程池
	public void exit() {
		threadpool.shutdownNow();
	}

	class Combination {
		// 标识这个任务，翻遍取消任务
		String flag = "";
		WeakReference<IMAsyncTask> weak;
	}

}
