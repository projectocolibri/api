package org.eclipse.nebula.widgets.formattedtext;

import java.math.BigDecimal;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;

/**
 * Formatter for percent values.<p>
 * This formatter extends the <code>NumberFormatter</code> :
 * - add the percent symbol in the suffix area.
 * - convert the number value in a percent : multiply by 100 on setValue() and
 *   divide by 100 on getValue().
 */
public class PercentFormatter extends NumberFormatter {
	public String pattern;
	/**
	 * Constructs a new instance with all defaults :
	 * <ul>
	 *   <li>edit mask from NumberPatterns for the default locale</li>
	 *   <li>display mask identical to the edit mask</li>
	 *   <li>default locale</li>
	 * </ul>
	 */
	public PercentFormatter() {
		super();
	}

	/**
	 * Constructs a new instance with default edit and display masks for the given
	 * locale.
	 *
	 * @param loc locale
	 */
	public PercentFormatter(Locale loc) {
		super(loc);
	}

	/**
	 * Constructs a new instance with the given edit mask and locale. Display mask
	 * is identical to the edit mask.
	 *
	 * @param editPattern edit mask
	 * @param loc locale
	 */
	public PercentFormatter(String editPattern, Locale loc) {
		super(editPattern, loc);
	}

	/**
	 * Constructs a new instance with the given masks and locale.
	 *
	 * @param editPattern edit mask
	 * @param displayPattern display mask
	 * @param loc locale
	 */
	public PercentFormatter(String editPattern, String displayPattern, Locale loc) {
		super(editPattern, displayPattern, loc, false);

	}

	/**
	 * Constructs a new instance with the given edit and display masks. Uses the
	 * default locale.
	 *
	 * @param editPattern edit mask
	 * @param displayPattern display mask
	 */
	public PercentFormatter(String editPattern, String displayPattern) {
		super(editPattern, displayPattern, false);
	}

	/**
	 * Constructs a new instance with the given edit mask. Display mask is
	 * identical to the edit mask, and locale is the default one.
	 *
	 * @param editPattern edit mask
	 */
	public PercentFormatter(String editPattern) {
		super(editPattern);
	}

	/**
	 * Returns the current value of the text control if it is a valid <code>Number</code>.
	 * If the buffer is flaged as modified, the value is recalculated by parsing
	 * with the <code>nfEdit</code> initialized with the edit pattern. If the
	 * number is not valid, returns <code>null</code>.
	 *
	 * @return current number value if valid, <code>null</code> else
	 * @see ITextFormatter#getValue()
	 */
	public Object getValue() {
		Number val = (Number) super.getValue();
		if ( val != null ) {
			val = new Double(val.doubleValue());
		}

		if(val!=null)
			return BigDecimal.valueOf(val.doubleValue());
		return val;
	}

	/**
	 * Sets the patterns and initializes the technical attributes used to manage
	 * the operations.<p>
	 * Override the NumberFormatter implementation to add the percent symbol to
	 * the masks.
	 *
	 * @param edit edit pattern
	 * @param display display pattern
	 * @param loc Locale to use
	 * @throws IllegalArgumentException if a pattern is invalid
	 * @see wdev91.comp4swt.core.NumberFormatter#setPatterns(java.lang.String, java.lang.String, java.util.Locale)
	 */
	protected void setPatterns(String edit, String display, Locale loc) {
		super.setPatterns(edit, display, loc);
		setSuffix("" + symbols.getPercent()); //$NON-NLS-1$
	}

	/**
	 * Sets the value to edit. The value provided must be a <code>Number</code>.
	 * The value provided is multiplied by 100.
	 *
	 * @param value number value
	 * @throws IllegalArgumentException if not a number
	 * @see ITextFormatter#setValue(java.lang.Object)
	 */
	public void setValue(Object value) {
		if ( value != null ) {
			if ( ! (value instanceof Number) ) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT, null, "Value must be a Number"); //$NON-NLS-1$
			}
			super.setValue(new Double(((Number) value).doubleValue()));
		} else {
			super.setValue(value);
		}
	}


	public void verifyText(VerifyEvent e) {

		if ( ignore ) {
			return;
		}

		int p; // Current insertion position in the edit cache
		e.doit = false;

		if ( e.keyCode == SWT.BS || e.keyCode == SWT.DEL ) {
			clearText(e.start, (e.end > e.start) ? e.end - e.start : 1);
			p = e.start;
		} else {
			if ( e.end > e.start ) {
				clearText(e.start, e.end - e.start);
			}
			p = e.start;

			int d = editValue.indexOf(EMPTY + symbols.getDecimalSeparator()); // Decimal separator position
			for (int i = 0; i < e.text.length(); i++) {
				if ( p < prefixLen || p > editValue.length() - suffixLen ) break;
				char c = e.text.charAt(i);
				if ( c >= '0' && c <= '9' ) {
					// Controls the number of digits by group
					if ( d >= 0 && p > d ) {
						if ( fixedDec && p > d + decimalLen ) {
							//beep();
							break;
						}
					} else {
						if ( fixedInt && intCount >= intLen ) {
							//beep();
							break;
						}
						d++;
						intCount++;
					}
					// Insert the char
					editValue.insert(p++, c);
				} else if ( groupLen > 0
						&& (c == symbols.getGroupingSeparator()
								|| (c == ' ' && nbspSeparator)) ) {
					/*
					 * Some locales (eg. french) return a no-break space as the grouping
					 * separator. This character is not naturel to use for users. So we
					 * recognize too the simple space as the grouping separator.
					 *
					 * Java bug: 4510618
					 */
					if ( d >= 0 && p > d ) {
						//beep();
						break;
					}
					int n = editValue.indexOf(EMPTY + symbols.getGroupingSeparator(), p);
					if ( n > p ) {
						p = n + 1;
					}
				} else if ( c == symbols.getMinusSign() ) {
					if ( p != prefixLen || ! minus ) {
						//beep(); // Minus sign only possible in first position
						break;
					}
					if ( this.editValue.length() == 0 || editValue.charAt(0) != c ) {
						editValue.insert(p++, c);
						negative = true;
						d++;
					} else {
						editValue.deleteCharAt(0);
						negative = false;
						d--;
					}
				} else if ( c == symbols.getDecimalSeparator()
						&& (decimalLen > 0 || ! fixedDec ) ) {
					if ( d >= 0 ) {
						if ( d < p ) {
							//beep();
							break;
						}
						p = d + 1;
						continue;
					}
					d = p;
					editValue.insert(p++, c);
					intCount = 0;
					for (int j = 0; j < d; j++) {
						char c1 = editValue.charAt(j);
						if ( c1 >= '0' && c1 <= '9' ) {
							intCount++;
						}
					}
				} else {
					//beep();
				}
			}
		}
		p = format(p);
		modified = true;
		updateText(getEditString(), p);
	}


}
