public interface ListOperations<K, V> {

	/**
	 * Get elements between {@code begin} and {@code end} from list at {@code key}.
	 * 获得指定范围的元素
	 *
	 * @param key must not be {@literal null}. key不能为null
	 * @param start
	 * @param end
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="http://redis.io/commands/lrange">Redis Documentation: LRANGE</a>
	 */
	@Nullable
	List<V> range(K key, long start, long end);

	/**
	 * Trim list at {@code key} to elements between {@code start} and {@code end}.
	 * 截取指定范围的list
	 *
	 * @param key must not be {@literal null}. 
	 * @param start
	 * @param end
	 * @see <a href="http://redis.io/commands/ltrim">Redis Documentation: LTRIM</a>
	 */
	void trim(K key, long start, long end);

	/**
	 * Get the size of list stored at {@code key}.
	 * 获得list存储在key的大小
	 * 
	 * @param key must not be {@literal null}.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="http://redis.io/commands/llen">Redis Documentation: LLEN</a>
	 */
	@Nullable
	Long size(K key);

	/**
	 * Prepend {@code value} to {@code key}.
	 * 将value，追加在key的前面（左）
	 *
	 * @param key must not be {@literal null}.
	 * @param value
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="http://redis.io/commands/lpush">Redis Documentation: LPUSH</a>
	 */
	
	@Nullable
	Long leftPush(K key, V value);

	/**
	 * Prepend {@code values} to {@code key}.
	 * 将values[可变参数]，追加在key的前面（左）
	 *
	 * @param key must not be {@literal null}.
	 * @param values
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="http://redis.io/commands/lpush">Redis Documentation: LPUSH</a>
	 */
	@Nullable
	Long leftPushAll(K key, V... values);

	/**
	 * Prepend {@code values} to {@code key}.
	 * 将values[Collection集合]，追加在key的前面。
	 *
	 * @param key must not be {@literal null}.
	 * @param values must not be {@literal null}.
	 * @return {@literal null} when used in pipeline / transaction.
	 * @since 1.5
	 * @see <a href="http://redis.io/commands/lpush">Redis Documentation: LPUSH</a>
	 */
	@Nullable
	Long leftPushAll(K key, Collection<V> values);

	/**
	 * Prepend {@code values} to {@code key} only if the list exists.
	 * 如果key存在，则将value追加在key的前面。
	 * 
	 * @param key must not be {@literal null}.
	 * @param value
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="http://redis.io/commands/lpushx">Redis Documentation: LPUSHX</a>
	 */
	@Nullable
	Long leftPushIfPresent(K key, V value);

	/**
	 * Prepend {@code values} to {@code key} before {@code value}.
	 * 在指定值（pivot）的左边添加value，多个添加在第一个左边，没有则不添加
	 *
	 * @param key must not be {@literal null}.
	 * @param value
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="http://redis.io/commands/lpush">Redis Documentation: LPUSH</a>
	 */
	@Nullable
	Long leftPush(K key, V pivot, V value);

	/**
	 * Append {@code value} to {@code key}.
	 * 在key的右边添加
	 *
	 * @param key must not be {@literal null}.
	 * @param value
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="http://redis.io/commands/rpush">Redis Documentation: RPUSH</a>
	 */
	@Nullable
	Long rightPush(K key, V value);

	/**
	 * Append {@code values} to {@code key}.
	 * 在右边添加多个，可变参数
	 *
	 * @param key must not be {@literal null}.
	 * @param values
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="http://redis.io/commands/rpush">Redis Documentation: RPUSH</a>
	 */
	@Nullable
	Long rightPushAll(K key, V... values);

	/**
	 * Append {@code values} to {@code key}.
	 * 在右边添加一个Collection集合
	 *
	 * @param key must not be {@literal null}.
	 * @param values
	 * @return {@literal null} when used in pipeline / transaction.
	 * @since 1.5
	 * @see <a href="http://redis.io/commands/rpush">Redis Documentation: RPUSH</a>
	 */
	@Nullable
	Long rightPushAll(K key, Collection<V> values);

	/**
	 * Append {@code values} to {@code key} only if the list exists.
	 * 在右边添加，仅当list（key）存在
	 *
	 * @param key must not be {@literal null}.
	 * @param value
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="http://redis.io/commands/rpushx">Redis Documentation: RPUSHX</a>
	 */
	@Nullable
	Long rightPushIfPresent(K key, V value);

	/**
	 * Append {@code values} to {@code key} before {@code value}.
	 * 在指定值（pivot）的右边添加value，没有则不添加
	 *
	 * @param key must not be {@literal null}.
	 * @param value
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="http://redis.io/commands/lpush">Redis Documentation: RPUSH</a>
	 */
	@Nullable
	Long rightPush(K key, V pivot, V value);

	/**
	 * Set the {@code value} list element at {@code index}.
	 * 直译：设置。其实个人觉得是覆盖，即只能覆盖已经存在的索引（既不创建key，也不创建key下不存在的index的value）
	 *
	 * @param key must not be {@literal null}.
	 * @param index
	 * @param value
	 * @see <a href="http://redis.io/commands/lset">Redis Documentation: LSET</a>
	 */
	void set(K key, long index, V value);

	/**
	 * Removes the first {@code count} occurrences of {@code value} from the list stored at {@code key}.
	 * 从左边开始，删除指定个数的指定value，不足个数，则只是全部删除。
	 *
	 * @param key must not be {@literal null}.
	 * @param count
	 * @param value
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="http://redis.io/commands/lrem">Redis Documentation: LREM</a>
	 */
	@Nullable
	Long remove(K key, long count, Object value);

	/**
	 * Get element at {@code index} form list at {@code key}.
	 * 获得指定下标的值。类似：list.get(index)
	 *
	 * @param key must not be {@literal null}.
	 * @param index
	 * @return {@literal null} when used in pipeline / transaction.
	 * @see <a href="http://redis.io/commands/lindex">Redis Documentation: LINDEX</a>
	 */
	@Nullable
	V index(K key, long index);

	/**
	 * Removes and returns first element in list stored at {@code key}.
	 * 从左边弹出key的一个value、
	 *
	 * @param key must not be {@literal null}.
	 * @return can be {@literal null}.
	 * @see <a href="http://redis.io/commands/lpop">Redis Documentation: LPOP</a>
	 */
	@Nullable
	V leftPop(K key);

	/**
	 * Removes and returns first element from lists stored at {@code key} . <br>
	 * <b>Blocks connection</b> until element available or {@code timeout} reached.
	 * 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
	 *
	 * @param key must not be {@literal null}.
	 * @param timeout
	 * @param unit must not be {@literal null}.
	 * @return can be {@literal null}.
	 * @see <a href="http://redis.io/commands/blpop">Redis Documentation: BLPOP</a>
	 */
	@Nullable
	V leftPop(K key, long timeout, TimeUnit unit);

	/**
	 * Removes and returns last element in list stored at {@code key}.
	 *  从右边弹出key的一个value、
	 *
	 * @param key must not be {@literal null}.
	 * @return can be {@literal null}.
	 * @see <a href="http://redis.io/commands/rpop">Redis Documentation: RPOP</a>
	 */
	@Nullable
	V rightPop(K key);

	/**
	 * Removes and returns last element from lists stored at {@code key}. <br>
	 * <b>Blocks connection</b> until element available or {@code timeout} reached.
	 *
	 * @param key must not be {@literal null}.
	 * @param timeout
	 * @param unit must not be {@literal null}.
	 * @return can be {@literal null}.
	 * @see <a href="http://redis.io/commands/brpop">Redis Documentation: BRPOP</a>
	 */
	@Nullable
	V rightPop(K key, long timeout, TimeUnit unit);

	/**
	 * Remove the last element from list at {@code sourceKey}, append it to {@code destinationKey} and return its value.
	 * 取出sourceKey的右边一个value，压入到destinationKey的左边。
	 *
	 * @param sourceKey must not be {@literal null}.
	 * @param destinationKey must not be {@literal null}.
	 * @return can be {@literal null}.
	 * @see <a href="http://redis.io/commands/rpoplpush">Redis Documentation: RPOPLPUSH</a>
	 */

	@Nullable
	V rightPopAndLeftPush(K sourceKey, K destinationKey);

	/**
	 * Remove the last element from list at {@code srcKey}, append it to {@code dstKey} and return its value.<br>
	 * <b>Blocks connection</b> until element available or {@code timeout} reached.
	 * 移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
	 *
	 * @param sourceKey must not be {@literal null}.
	 * @param destinationKey must not be {@literal null}.
	 * @param timeout
	 * @param unit must not be {@literal null}.
	 * @return can be {@literal null}.
	 * @see <a href="http://redis.io/commands/brpoplpush">Redis Documentation: BRPOPLPUSH</a>
	 */
	@Nullable
	V rightPopAndLeftPush(K sourceKey, K destinationKey, long timeout, TimeUnit unit);

	RedisOperations<K, V> getOperations();
}
