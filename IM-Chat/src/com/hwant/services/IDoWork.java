package com.hwant.services;

public interface IDoWork {
	// 线程中做
	Object doWhat();

	// 线程执行完，做
	// 远程服务需要广播，本地服务不需
	void Finish2Do(Object obj);
}
