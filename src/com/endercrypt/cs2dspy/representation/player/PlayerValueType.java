package com.endercrypt.cs2dspy.representation.player;

public enum PlayerValueType
{
	ID((p) -> p.getID()),
	NAME((p) -> p.getName()),
	USGN((p) -> p.getUsgn(), (p, v) -> v.equals("0") ? "None" : v),
	IP((p) -> p.getIp()),
	PORT((p) -> p.getPort()),
	ADDRESS((p) -> p.getAddress()),
	LATENCY((p) -> p.getPing(), (p, v) -> p.isBot() ? "[Bot]" : v),
	;

	private PlayerValueInterface valueAccess;
	private ValueTreatment valueTreatment;

	private PlayerValueType(PlayerValueInterface valueAccess)
	{
		this(valueAccess, null);
	}

	private PlayerValueType(PlayerValueInterface valueAccess, ValueTreatment valueTreatment)
	{
		this.valueAccess = valueAccess;
		this.valueTreatment = valueTreatment;
	}

	public String get(SpyPlayer player)
	{
		String value = String.valueOf(valueAccess.getValue(player));
		if (valueTreatment != null)
		{
			value = valueTreatment.treat(player, value);
		}
		return value;
	}

	@FunctionalInterface
	private interface PlayerValueInterface
	{
		public Object getValue(SpyPlayer player);
	}

	private interface ValueTreatment
	{
		public String treat(SpyPlayer player, String value);
	}
}
