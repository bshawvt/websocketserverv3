package shared;

public interface Callback<T> {
	abstract void fn(T arg);
}
