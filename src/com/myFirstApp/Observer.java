package com.myFirstApp;

import java.util.Map;

public interface Observer {

	<T, K> void update(Map<T, K> listOfElements);
}
