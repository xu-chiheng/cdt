/**********************************************************************
 * Copyright (c) 2002,2003 QNX Software Systems and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: 
 * QNX Software Systems - Initial API and implementation
***********************************************************************/

package org.eclipse.cdt.utils.debug.dwarf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.cdt.utils.debug.IDebugEntryRequestor;
import org.eclipse.cdt.utils.debug.tools.DebugSym;
import org.eclipse.cdt.utils.debug.tools.DebugSymsRequestor;
import org.eclipse.cdt.utils.elf.Elf;

public class Dwarf {

	/* Section names. */
	final static String DWARF_DEBUG_INFO = ".debug_info";
	final static String DWARF_DEBUG_ABBREV = ".debug_abbrev";
	final static String DWARF_DEBUG_ARANGES = ".debug_aranges";
	final static String DWARF_DEBUG_LINE = ".debug_line";
	final static String DWARF_DEBUG_FRAME = ".debug_frame";
	final static String DWARF_EH_FRAME = ".eh_frame";
	final static String DWARF_DEBUG_LOC = ".debug_loc";
	final static String DWARF_DEBUG_PUBNAMES = ".debug_pubnames";
	final static String DWARF_DEBUG_STR = ".debug_str";
	final static String DWARF_DEBUG_FUNCNAMES = ".debug_funcnames";
	final static String DWARF_DEBUG_TYPENAMES = ".debug_typenames";
	final static String DWARF_DEBUG_VARNAMES = ".debug_varnames";
	final static String DWARF_DEBUG_WEAKNAMES = ".debug_weaknames";
	final static String DWARF_DEBUG_MACINFO = ".debug_macinfo";
	final static String[] DWARF_SCNNAMES =
		{
			DWARF_DEBUG_INFO,
			DWARF_DEBUG_ABBREV,
			DWARF_DEBUG_ARANGES,
			DWARF_DEBUG_LINE,
			DWARF_DEBUG_FRAME,
			DWARF_EH_FRAME,
			DWARF_DEBUG_LOC,
			DWARF_DEBUG_PUBNAMES,
			DWARF_DEBUG_STR,
			DWARF_DEBUG_FUNCNAMES,
			DWARF_DEBUG_TYPENAMES,
			DWARF_DEBUG_VARNAMES,
			DWARF_DEBUG_WEAKNAMES,
			DWARF_DEBUG_MACINFO };

	class CompilationUnitHeader {
		int length;
		short version;
		int abbreviationOffset;
		byte addressSize;
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("Length: " + length).append("\n");
			sb.append("Version: " + version).append("\n");
			sb.append("Abbreviation: " + abbreviationOffset).append("\n");
			sb.append("Address size: " + addressSize).append("\n");
			return sb.toString();
		}
	}

	class AbbreviationEntry {
		/* unsigned */
		long code;
		/* unsigned */
		long tag;
		byte hasChildren;
		List attributes;
		AbbreviationEntry(long c, long t, byte h) {
			code = c;
			tag = t;
			hasChildren = h;
			attributes = new ArrayList();
		}
	}

	class Attribute {
		/* unsigned */
		long name;
		/* unsigned */
		long form;
		Attribute(long n, long f) {
			name = n;
			form = f;
		}
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("name: " + Long.toHexString(name));
			sb.append(" value: " + Long.toHexString(form));
			return sb.toString();
		}
	}

	class AttributeValue {
		Attribute attribute;
		Object value;
		AttributeValue(Attribute a, Object o) {
			attribute = a;
			value = o;
		}
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append(attribute.toString()).append(' ');
			if (value != null) {
				Class clazz = value.getClass();
				if (clazz.isArray()) {
					int len = Array.getLength(value);
					sb.append(len).append(' ');
					sb.append(clazz.getComponentType().toString());
					sb.append(':');
					for (int i = 0; i < len; i++) {
						byte b = Array.getByte(value, i);
						sb.append(' ').append(Integer.toHexString((int) b));
					}
				} else {
					if (value instanceof Number) {
						Number n = (Number) value;
						sb.append(Long.toHexString(n.longValue()));
					} else if (value instanceof String) {
						sb.append(value);
					} else {
						sb.append(value);
					}
				}
			}
			return sb.toString();
		}
	}

	Map dwarfSections = new HashMap();
	Map abbreviationMaps = new HashMap();

	boolean isLE;

	public Dwarf(String file) throws IOException {
		Elf exe = new Elf(file);
		init(exe);
		exe.dispose();
	}

	public Dwarf(Elf exe) throws IOException {
		init(exe);
	}

	public void init(Elf exe) throws IOException {
		Elf.ELFhdr header = exe.getELFhdr();
		isLE = header.e_ident[Elf.ELFhdr.EI_DATA] == Elf.ELFhdr.ELFDATA2LSB;

		Elf.Section[] sections = exe.getSections();
		for (int i = 0; i < sections.length; i++) {
			String name = sections[i].toString();
			for (int j = 0; j < DWARF_SCNNAMES.length; j++) {
				if (name.equals(DWARF_SCNNAMES[j])) {
					dwarfSections.put(DWARF_SCNNAMES[j], sections[i].loadSectionData());
				}
			}
		}
	}

	int read_4_bytes(InputStream in) throws IOException {
		try {
			byte[] bytes = new byte[4];
			int n = in.read(bytes, 0, bytes.length);
			if (n != 4) {
				throw new IOException("missing bytes");
			}
			return read_4_bytes(bytes, 0);
		} catch (IndexOutOfBoundsException e) {
			throw new IOException("missing bytes");
		}
	}

	// FIXME:This is wrong, it's signed.
	int read_4_bytes(byte[] bytes, int offset) throws IndexOutOfBoundsException {
		if (isLE) {
			return (
				((bytes[offset + 3] & 0xff) << 24)
					| ((bytes[offset + 2] & 0xff) << 16)
					| ((bytes[offset + 1] & 0xff) << 8)
					| (bytes[offset] & 0xff));
		}
		return (
			((bytes[offset] & 0xff) << 24)
				| ((bytes[offset + 1] & 0xff) << 16)
				| ((bytes[offset + 2] & 0xff) << 8)
				| (bytes[offset + 3] & 0xff));
	}

	long read_8_bytes(InputStream in) throws IOException {
		try {
			byte[] bytes = new byte[8];
			int n = in.read(bytes, 0, bytes.length);
			if (n != 8) {
				throw new IOException("missing bytes");
			}
			return read_8_bytes(bytes, 0);
		} catch (IndexOutOfBoundsException e) {
			throw new IOException("missing bytes");
		}
	}

	// FIXME:This is wrong, it's signed.
	long read_8_bytes(byte[] bytes, int offset) throws IndexOutOfBoundsException {

		if (isLE) {
			return (
				((bytes[offset + 7] & 0xff) << 56)
					| ((bytes[offset + 6] & 0xff) << 48)
					| ((bytes[offset + 5] & 0xff) << 40)
					| ((bytes[offset + 4] & 0xff) << 32)
					| ((bytes[offset + 3] & 0xff) << 24)
					| ((bytes[offset + 2] & 0xff) << 16)
					| ((bytes[offset + 1] & 0xff) << 8)
					| (bytes[offset] & 0xff));
		}

		return (
			((bytes[offset] & 0xff) << 56)
				+ ((bytes[offset + 1] & 0xff) << 48)
				+ ((bytes[offset + 2] & 0xff) << 40)
				+ ((bytes[offset + 3] & 0xff) << 32)
				+ ((bytes[offset + 4] & 0xff) << 24)
				+ ((bytes[offset + 5] & 0xff) << 16)
				+ ((bytes[offset + 6] & 0xff) << 8)
				+ (bytes[offset] & 0xff));

	}

	short read_2_bytes(InputStream in) throws IOException {
		try {
			byte[] bytes = new byte[2];
			int n = in.read(bytes, 0, bytes.length);
			if (n != 2) {
				throw new IOException("missing bytes");
			}
			return read_2_bytes(bytes, 0);
		} catch (IndexOutOfBoundsException e) {
			throw new IOException("missing bytes");
		}
	}

	short read_2_bytes(byte[] bytes, int offset) throws IndexOutOfBoundsException {
		if (isLE) {
			return (short) (((bytes[offset + 1] & 0xff) << 8) + (bytes[offset] & 0xff));
		}
		return (short) (((bytes[offset] & 0xff) << 8) + (bytes[offset + 1] & 0xff));
	}

	private int num_leb128_read;

	/* unsigned */
	long read_unsigned_leb128(InputStream in) throws IOException {
		/* unsigned */
		long result = 0;
		num_leb128_read = 0;
		int shift = 0;
		short b;

		while (true) {
			b = (short) in.read();
			if (b == -1)
				break; //throw new IOException("no more data");
			num_leb128_read++;
			result |= ((long) (b & 0x7f) << shift);
			if ((b & 0x80) == 0) {
				break;
			}
			shift += 7;
		}
		return result;
	}

	/* unsigned */
	long read_signed_leb128(InputStream in) throws IOException {
		/* unsigned */
		long result = 0;
		int shift = 0;
		int size = 32;
		num_leb128_read = 0;
		short b;

		while (true) {
			b = (short) in.read();
			if (b == -1)
				throw new IOException("no more data");
			num_leb128_read++;
			result |= ((long) (b & 0x7f) << shift);
			shift += 7;
			if ((b & 0x80) == 0) {
				break;
			}
		}
		if ((shift < size) && (b & 0x40) != 0) {
			result |= - (1 << shift);
		}
		return result;
	}

	public void parse(IDebugEntryRequestor requestor) {
		parseDebugInfo(requestor);
	}

	void parseDebugInfo(IDebugEntryRequestor requestor) {
		byte[] data = (byte[]) dwarfSections.get(DWARF_DEBUG_INFO);
		if (data != null) {
			try {
				int length = 0;
				for (int offset = 0; offset < data.length; offset += (length + 4)) {
					CompilationUnitHeader header = new CompilationUnitHeader();
					header.length = length = read_4_bytes(data, offset);
					header.version = read_2_bytes(data, offset + 4);
					header.abbreviationOffset = read_4_bytes(data, offset + 6);
					header.addressSize = data[offset + 10];

					System.out.println("Compilation Unit @ " + Long.toHexString(offset));
					System.out.println(header);

					// read the abbrev section.
					InputStream in = new ByteArrayInputStream(data, offset + 11, length);
					Map abbrevs = parseDebugAbbreviation(header);
					parseDebugInfoEntry(requestor, in, abbrevs, header);

					System.out.println();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	Map parseDebugAbbreviation(CompilationUnitHeader header) throws IOException {
		int offset = header.abbreviationOffset;
		Integer key = new Integer(offset);
		Map abbrevs = (Map) abbreviationMaps.get(key);
		if (abbrevs == null) {
			abbrevs = new HashMap();
			abbreviationMaps.put(key, abbrevs);
			byte[] data = (byte[]) dwarfSections.get(DWARF_DEBUG_ABBREV);
			if (data != null) {
				InputStream in = new ByteArrayInputStream(data);
				in.skip(offset);
				while (in.available() > 0) {
					long code = read_unsigned_leb128(in);
					if (code == 0) {
						break;
					}
					long tag = read_unsigned_leb128(in);
					byte hasChildren = (byte) in.read();
					AbbreviationEntry entry = new AbbreviationEntry(code, tag, hasChildren);

					//System.out.println("\tAbrev Entry: " + code + " " + Long.toHexString(entry.tag) + " " + entry.hasChildren);

					// attributes
					long name = 0;
					long form = 0;
					do {
						name = read_unsigned_leb128(in);
						form = read_unsigned_leb128(in);
						if (name != 0) {
							entry.attributes.add(new Attribute(name, form));
						}
						//System.out.println("\t\t " + Long.toHexString(name) + " " + Long.toHexString(value));
					} while (name != 0 && form != 0);
					abbrevs.put(new Long(code), entry);
				}
			}
		}
		return abbrevs;
	}

	void parseDebugInfoEntry(IDebugEntryRequestor requestor, InputStream in, Map abbrevs, CompilationUnitHeader header)
		throws IOException {
		while (in.available() > 0) {
			long code = read_unsigned_leb128(in);
			AbbreviationEntry entry = (AbbreviationEntry) abbrevs.get(new Long(code));
			if (entry != null) {
				int len = entry.attributes.size();
				List list = new ArrayList(len);
				try {
					for (int i = 0; i < len; i++) {
						Attribute attr = (Attribute) entry.attributes.get(i);
						Object obj = readAttribute((int) attr.form, in, header);
						list.add(new AttributeValue(attr, obj));
					}
				} catch (IOException e) {
					//break;
				}
				processDebugInfoEntry(requestor, entry, list);
			}
		}
	}

	Object readAttribute(int form, InputStream in, CompilationUnitHeader header) throws IOException {
		Object obj = null;
		switch (form) {
			case DwarfConstants.DW_FORM_addr :
			case DwarfConstants.DW_FORM_ref_addr :
				obj = readAddress(in, header);
				break;

			case DwarfConstants.DW_FORM_block :
				{
					int size = (int) read_unsigned_leb128(in);
					byte[] bytes = new byte[size];
					in.read(bytes, 0, size);
					obj = bytes;
				}
				break;

			case DwarfConstants.DW_FORM_block1 :
				{
					int size = in.read();
					byte[] bytes = new byte[size];
					in.read(bytes, 0, size);
					obj = bytes;
				}
				break;

			case DwarfConstants.DW_FORM_block2 :
				{
					int size = read_2_bytes(in);
					byte[] bytes = new byte[size];
					in.read(bytes, 0, size);
					obj = bytes;
				}
				break;

			case DwarfConstants.DW_FORM_block4 :
				{
					int size = read_4_bytes(in);
					byte[] bytes = new byte[size];
					in.read(bytes, 0, size);
					obj = bytes;
				}
				break;

			case DwarfConstants.DW_FORM_data1 :
				obj = new Byte((byte) in.read());
				break;

			case DwarfConstants.DW_FORM_data2 :
				obj = new Short(read_2_bytes(in));
				break;

			case DwarfConstants.DW_FORM_data4 :
				obj = new Integer(read_4_bytes(in));
				break;

			case DwarfConstants.DW_FORM_data8 :
				obj = new Long(read_8_bytes(in));
				break;

			case DwarfConstants.DW_FORM_sdata :
				obj = new Long(read_signed_leb128(in));
				break;

			case DwarfConstants.DW_FORM_udata :
				obj = new Long(read_unsigned_leb128(in));
				break;

			case DwarfConstants.DW_FORM_string :
				{
					int c;
					StringBuffer sb = new StringBuffer();
					while ((c = in.read()) != -1) {
						if (c == 0) {
							break;
						}
						sb.append((char) c);
					}
					obj = sb.toString();
				}
				break;

			case DwarfConstants.DW_FORM_flag :
				obj = new Byte((byte) in.read());
				break;

			case DwarfConstants.DW_FORM_strp :
				{
					int offset = (int) read_4_bytes(in);
					byte[] data = (byte[]) dwarfSections.get(DWARF_DEBUG_STR);
					if (data == null) {
						obj = new String();
					} else if (offset < 0 || offset > data.length) {
						obj = new String();
					} else {
						StringBuffer sb = new StringBuffer();
						for (; offset < data.length; offset++) {
							byte c = data[offset];
							if (c == 0) {
								break;
							}
							sb.append((char) c);
						}
						obj = sb.toString();
					}
				}
				break;

			case DwarfConstants.DW_FORM_ref1 :
				obj = new Byte((byte) in.read());
				break;

			case DwarfConstants.DW_FORM_ref2 :
				obj = new Short(read_2_bytes(in));
				break;

			case DwarfConstants.DW_FORM_ref4 :
				obj = new Integer(read_4_bytes(in));
				break;

			case DwarfConstants.DW_FORM_ref8 :
				obj = new Long(read_8_bytes(in));
				break;

			case DwarfConstants.DW_FORM_ref_udata :
				obj = new Long(read_unsigned_leb128(in));
				break;

			case DwarfConstants.DW_FORM_indirect :
				{
					int f = (int) read_unsigned_leb128(in);
					return readAttribute(f, in, header);
				}

			default :
				break;
		}

		return obj;
	}

	void processDebugInfoEntry(IDebugEntryRequestor requestor, AbbreviationEntry entry, List list) {
		int len = list.size();
		int tag = (int) entry.tag;
		System.out.println("Abbrev Number " + entry.code);
		for (int i = 0; i < len; i++) {
			AttributeValue av = (AttributeValue) list.get(i);
			System.out.println(av);
			// We are only interrested in certain tags.
			switch (tag) {
				case DwarfConstants.DW_TAG_array_type :
					break;
				case DwarfConstants.DW_TAG_class_type :
					break;
				case DwarfConstants.DW_TAG_enumeration_type :
					break;
				case DwarfConstants.DW_TAG_formal_parameter :
					break;
				case DwarfConstants.DW_TAG_lexical_block :
					break;
				case DwarfConstants.DW_TAG_member :
					break;
				case DwarfConstants.DW_TAG_pointer_type :
					break;
				case DwarfConstants.DW_TAG_reference_type :
					break;
				case DwarfConstants.DW_TAG_compile_unit :
					break;
				case DwarfConstants.DW_TAG_structure_type :
					break;
				case DwarfConstants.DW_TAG_subroutine_type :
					break;
				case DwarfConstants.DW_TAG_typedef :
					break;
				case DwarfConstants.DW_TAG_union_type :
					break;
				case DwarfConstants.DW_TAG_unspecified_parameters :
					break;
				case DwarfConstants.DW_TAG_inheritance :
					break;
				case DwarfConstants.DW_TAG_ptr_to_member_type :
					break;
				case DwarfConstants.DW_TAG_with_stmt :
					break;
				case DwarfConstants.DW_TAG_base_type :
					break;
				case DwarfConstants.DW_TAG_catch_block :
					break;
				case DwarfConstants.DW_TAG_const_type :
					break;
				case DwarfConstants.DW_TAG_enumerator :
					break;
				case DwarfConstants.DW_TAG_file_type :
					break;
				case DwarfConstants.DW_TAG_friend :
					break;
				case DwarfConstants.DW_TAG_subprogram :
					break;
				case DwarfConstants.DW_TAG_template_type_param :
					break;
				case DwarfConstants.DW_TAG_template_value_param :
					break;
				case DwarfConstants.DW_TAG_thrown_type :
					break;
				case DwarfConstants.DW_TAG_try_block :
					break;
				case DwarfConstants.DW_TAG_variable :
					break;
				case DwarfConstants.DW_TAG_volatile_type :
					break;
			}
		}
	}

	Long readAddress(InputStream in, CompilationUnitHeader header) throws IOException {
		long value = 0;

		switch (header.addressSize) {
			case 2 :
				value = read_2_bytes(in);
				break;
			case 4 :
				value = read_4_bytes(in);
				break;
			case 8 :
				value = read_8_bytes(in);
				break;
			default :
				// ????
		}
		return new Long(value);
	}

	public static void main(String[] args) {
		try {
			DebugSymsRequestor symreq = new DebugSymsRequestor();				
			Dwarf dwarf = new Dwarf(args[0]);
			dwarf.parse(symreq);
			DebugSym[] entries = symreq.getEntries();
			for (int i = 0; i < entries.length; i++) {
				DebugSym entry = entries[i];
				System.out.println(entry);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
