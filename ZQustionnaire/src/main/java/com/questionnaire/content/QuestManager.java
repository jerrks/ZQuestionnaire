package com.questionnaire.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.text.TextUtils;

import com.questionnaire.Conf;
import com.questionnaire.R;
import com.questionnaire.db.Answer;
import com.questionnaire.db.Paper;
import com.questionnaire.db.Subject;
import com.questionnaire.db.SubjectAnswerPairs;
import com.questionnaire.db.Tester;
import com.questionnaire.db.impl.Dao;
import com.questionnaire.db.impl.DaoAnswer;
import com.questionnaire.db.impl.DaoPaper;
import com.questionnaire.db.impl.DaoTester;
import com.questionnaire.utils.Util;

public class QuestManager {

	private static QuestManager mInstance = null;

	public static QuestManager getmInstance() {
		if (mInstance == null) {
			mInstance = new QuestManager();
		}
		return mInstance;
	}

	private QuestManager() {
	}

	public Paper getPaper(long id) {
		DaoPaper paper = (DaoPaper) Dao.getDaoPaper();
		return paper.get(id);
	}

	public Tester getTester(long id) {
		DaoTester tester = (DaoTester) Dao.getDaoTester();
		return tester.get(id);
	}

	public String getTesterInfo(long id) {
		Tester tester = getTester(id);
		StringBuffer info = new StringBuffer();
		info.append("ID:" + tester.getId());
		info.append(formatStr(R.string.label_age, tester.getAge()) + ",");
		info.append(formatStr(R.string.label_contant, tester.getContant())+ ",");
		info.append(formatStr(R.string.label_gender, tester.getGengerString()) + ",");
		info.append(formatStr(R.string.label_nation, tester.getNation()) + ",");
		info.append(formatStr(R.string.label_place, tester.getPlace()) + ",");
		info.append(formatStr(R.string.label_profession, tester.getProfession()));
		return info.toString();
	}

	public String getStatTesterInfo(long id) {
		Tester tester = getTester(id);
		StringBuffer info = new StringBuffer();
		info.append(formatStr(R.string.label_age, tester.getAge()) + ", ");
		info.append(formatStr(R.string.label_gender,
				tester.getGengerString()) + ", ");
		info.append(formatStr(R.string.label_place, tester.getPlace()) + ", ");
		info.append(formatStr(R.string.label_profession, tester.getProfession()));
		return info.toString();
	}

	public String formatStr(int res, Object obj) {
		return Conf.formatString(res, obj);
	}

	public Answer getAnswer(int id) {
		DaoAnswer answer = (DaoAnswer) Dao.getDaoAnswer();
		return answer.get(id);
	}

	public List<Answer> getAnswersList(long paperId, int subjectId) {
		DaoAnswer answer = (DaoAnswer) Dao.getDaoAnswer();
		List<Answer> list = answer.getSubjectAnswers(paperId, subjectId);
		return list;
	}

	public List<SubjectAnswerPairs> getSubjectAnswerPairs(long paperId) {
		List<SubjectAnswerPairs> list = Dao.getDaoAnswer().getSubjectsAnswers(paperId);
		return list;
	}

	public List<String> getAnswerStaInfo(List<SubjectAnswerPairs> list) {
		List<String> li = new ArrayList<String>();
		for (SubjectAnswerPairs pairs : list) {
			li.add(parseAnswers(pairs.getSubject(), pairs.getAnswers()));
		}
		return li;
	}

	public String parseAnswersEllipsed(Subject subject, List<Answer> list) {
		return parseAnswers(subject, list, true);
	}

	public String parseAnswers(Subject subject, List<Answer> list) {
		return parseAnswers(subject, list, false);
	}

	public String parseAnswers(Subject subject, List<Answer> list, boolean isEllipsis) {
		String info = "";
		switch (subject.getType()) {
		case Subject.TYPE_CHOICE_SINGLE:
		case Subject.TYPE_CHOICE_MUTILPE:
			info = parseAnswerChoicesInfo(list, subject.getOptLabels(), null);
			break;

		case Subject.TYPE_SORT:
		case Subject.TYPE_ANSWER:
		case Subject.TYPE_CLOSE:
			info = parseCommonAnswers(list, isEllipsis);
			break;
		default:
			break;
		}
		return info;
	}

	public String parseCommonAnswers(List<Answer> list, boolean isEllipsis) {
		if (list == null || list.isEmpty())
			return "";
		StringBuffer info = new StringBuffer();
		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			Answer answer = list.get(i);
			if (answer == null) {
				continue;
			}
			info.append((1 + i) + ": " + answer.getAnswer() + "\t");
			if (isEllipsis && count >=5) {
				break;
			}
			count ++;
		}
		return info.toString();
	}

	public List<String> parseQuestionAnswers(List<Answer> list) {
		List<String> answers = new ArrayList<String>();
		if (list == null || list.isEmpty()) {
			return answers;
		}
		for (Answer answer : list) {
			answers.add(answer.getAnswer());
		}
		return answers;
	}

	public String parseAnswerChoicesInfo(List<Answer> list, String[] labels,
										 Comparator<Entry<String, Integer>> sortComparator) {
		List<Map.Entry<String, Integer>> resoultList = getChoiceResoultList(list, labels);
		if (resoultList == null || resoultList.isEmpty())
			return "answers is null";
		StringBuffer info = new StringBuffer();
		int totel = list.size();
		if (sortComparator != null) {
			Collections.sort(resoultList, sortComparator);//比例升序/降序
		}
		for (Entry<String, Integer> entry : resoultList) {
			float pers = (float)100 * entry.getValue() / totel;
			info.append(entry.getKey() + ": " + Util.formateFloatStr(pers, "#0.0") + "%,  ");
		}
		return info.toString();
	}

	public String parseAnswerChoicesDetail(Subject subject, List<Answer> list) {
		String detail = parseAnswers(subject, list);
		return detail.replace(",  ", "\n");
	}
	
	public List<Map.Entry<String, Integer>> getChoiceResoultList(List<Answer> list, String[] labels) {
		if (list == null || list.isEmpty())
			return null;
		Map<String, Integer> selNumMaps = initSelectedMap(labels);
		for (int i = 0; i < list.size(); i++) {
			Answer answer = list.get(i);
			if (answer == null)
				continue;
			String content = answer.getAnswer();
			if (TextUtils.isEmpty(content))
				continue;
			Map<String, Integer> optMap = getSelectedMap(content, labels);
			for (Entry<String, Integer> opt : optMap.entrySet()) {
				String key = opt.getKey();
				selNumMaps.put(key, opt.getValue() + selNumMaps.get(key));
			}
		}
		
		List<Map.Entry<String, Integer>> sortedList = sortMap(selNumMaps, false);
		return sortedList;
	}
	
	public Map<String, Integer> initSelectedMap(String[] labels) {
		return getSelectedMap("", labels);
	}

	Map<String, Integer> getSelectedMap(String content, String[] labels) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.clear();
		for (int i = 0; i < labels.length; i++) {
			String label = labels[i];
			if (content.contains(label)) {
				map.put(label, 1);
			} else {
				map.put(label, 0);
			}
		}
		return map;
	}
	
	public List<Map.Entry<String, Map<Integer, Integer>>> parseSortSubResults(List<Answer> list, String[] labels) {
		Map<String, Map<Integer, Integer>> hm = new HashMap<String, Map<Integer,Integer>>();
		initSortSubResult(hm, labels);
		for (Answer answer : list) {
			String content = answer.getAnswer();
			String[] opts = Util.toStringArray(content.toCharArray());
			parseSortSubResult(hm, opts);
		}
		return sortMap(hm);
	}
	
	public void initSortSubResult(Map<String, Map<Integer, Integer>> hm, String[] labels) {
		if(hm == null) {
			hm = new HashMap<String, Map<Integer,Integer>>();
		}
		hm.clear();
		for (int i = 0; i < labels.length; i++) {
			Map<Integer, Integer> numMap = new HashMap<Integer, Integer>();
			for (int j = 0; j < labels.length; j++) {
				numMap.put(j, 0);
			}
			
			hm.put(labels[i], numMap);
		}
	}
	
	public void parseSortSubResult(Map<String, Map<Integer, Integer>> hm, String[] labels) {
		Map<Integer, Integer> numMap = new HashMap<Integer, Integer>();
		for (int i = 0; i < labels.length; i++) {
			String label = labels[i];
			if(hm.containsKey(label)) {
				numMap = hm.get(label);
				int num = numMap.get(i) + 1;
				numMap.put(i, num);
			}
			hm.put(label, numMap);
		}
	}

	/**
	 * get a list that is sorted according to isSortByValue
	 * @param map
	 * @param isSortByValue
	 * @return
	 */
	public List<Map.Entry<String, Object>> sort(Map<String, Object> map,
			final boolean isSortByValue) {
		List<Map.Entry<String, Object>> list = new ArrayList<Map.Entry<String, Object>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Object>>() {
			public int compare(Map.Entry<String, Object> o1,
					Map.Entry<String, Object> o2) {
				if (isSortByValue) {
					String obj1 = o1.getValue().toString();
					String obj2 = o2.getValue().toString();
					if(o1.getValue() instanceof Integer){
						return Integer.valueOf(obj2) - Integer.valueOf(obj1);
					} else {
						return obj1.compareTo(obj2);
					}
				} else {
					return (o1.getKey()).toString().compareTo(o2.getKey());
				}
			}
		});
		return list;
	}
	
	public List<Map.Entry<String, Integer>> sortMap(Map<String, Integer> map,
			final boolean isSortByValue) {
		List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1,
					Map.Entry<String, Integer> o2) {
				if (isSortByValue) {
					int obj1 = o1.getValue();
					int obj2 = o2.getValue();
					return obj2 - obj1;
				} else {
					return o1.getKey().compareTo(o2.getKey());
				}
			}
		});
		return list;
	}
	
	public List<Map.Entry<String, Map<Integer, Integer>>> sortMap(Map<String, Map<Integer, Integer>> map) {
		List<Map.Entry<String, Map<Integer, Integer>>> list = new ArrayList<Map.Entry<String, Map<Integer, Integer>>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Map<Integer, Integer>>>() {
			public int compare(Map.Entry<String, Map<Integer, Integer>> o1,
					Map.Entry<String, Map<Integer, Integer>> o2) {
				return o1.getKey().compareTo(o2.getKey());
			}
		});
		return list;
	}

	public boolean isSelected(String opt, String content) {
		return false;

	}

	public List<String> getAnswerContentItems(int paperId, int questNum) {
		List<String> list = new ArrayList<String>();
		list.add("aaaaaaaaaaaa");
		list.add("bbbbbbbbbbbbbbbbbbbbbbbb");
		list.add("cccccccccccccc");
		list.add("dddddddddddddddddddd");
		return list;
	}
}
