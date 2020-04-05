package com.zfg.learn.common;

import java.util.List;

/*给wangEditor富文本编辑器的响应*/
public class RTFresponse {
		
		//errno为0表示没有错误
		private Integer errno;
		private List<String> data;
		
		public RTFresponse() {
			super();
		}
		public RTFresponse(Integer errno, List<String> data) {
			super();
			this.errno = errno;
			this.data = data;
		}
		public Integer getErrno() {
			return errno;
		}
		public void setErrno(Integer errno) {
			this.errno = errno;
		}
		public List<String> getData() {
			return data;
		}
		public void setData(List<String> data) {
			this.data = data;
		}
		@Override
		public String toString() {
			return "EditorImg [errno=" + errno + ", data=" + data + "]";
		}
}
