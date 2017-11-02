package amex.fs.commons;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


public class TransformParser {

	final static String tagAttrToOmit = "Receive TimeStamp" ;
	
	public static void main(String[] args) {

		TransformParser tfp = new TransformParser();
		tfp.compare("SIT_Transform1.xml", "SIT_Transform2.xml");
		
	}

	public boolean compare(String fileName1, String fileName2){
		try{
			File file1 = new File(fileName1);
			File file2 = new File(fileName2);

			if(! (file1.exists()&&file2.exists()) ){
				return false;
			}

			if (file1.isDirectory() || file2.isDirectory()) {
				return false;
			}
			
			JAXBContext jaxbContext;
			jaxbContext = JAXBContext.newInstance(TransformBean.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();  
			
			TransformBean tfB1 = (TransformBean) jaxbUnmarshaller.unmarshal(file1);  
			TransformBean tfB2 = (TransformBean) jaxbUnmarshaller.unmarshal(file2);
			
			System.out.println("Comparing");
			System.out.println( tfB1 );
			System.out.println( tfB2 );
			
			boolean result = tfB1.equals(tfB2);
			
			System.out.println("returning:"+result);
			
			return result;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}


	}
}


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
		"header",
		"body"
})
@XmlRootElement(name = "Envelope")
class TransformBean {

	@XmlElement(name = "Header", required = true)
	protected TransformBean.Header header;
	@XmlElement(name = "Body", required = true)
	protected TransformBean.Body body;

	@Override
	public String toString() {
		return "TransformBean [header=" + header + ", body=" + body + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((header == null) ? 0 : header.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransformBean other = (TransformBean) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (header == null) {
			if (other.header != null)
				return false;
		} else if (!header.equals(other.header))
			return false;
		return true;
	}

	/**
	 * Gets the value of the header property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link TransformBean.Header }
	 *     
	 */
	public TransformBean.Header getHeader() {
		return header;
	}

	/**
	 * Sets the value of the header property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link TransformBean.Header }
	 *     
	 */
	public void setHeader(TransformBean.Header value) {
		this.header = value;
	}

	/**
	 * Gets the value of the body property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link TransformBean.Body }
	 *     
	 */
	public TransformBean.Body getBody() {
		return body;
	}

	/**
	 * Sets the value of the body property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link TransformBean.Body }
	 *     
	 */
	public void setBody(TransformBean.Body value) {
		this.body = value;
	}


	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = {
			"request"
	})
	public static class Body {

		@XmlElement(name = "Request", required = true)
		protected TransformBean.Body.Request request;


		@Override
		public String toString() {
			return "Body [request=" + request + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((request == null) ? 0 : request.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Body other = (Body) obj;
			if (request == null) {
				if (other.request != null)
					return false;
			} else if (!request.equals(other.request))
				return false;
			return true;
		}

		/**
		 * Gets the value of the request property.
		 * 
		 * @return
		 *     possible object is
		 *     {@link TransformBean.Body.Request }
		 *     
		 */
		public TransformBean.Body.Request getRequest() {
			return request;
		}

		/**
		 * Sets the value of the request property.
		 * 
		 * @param value
		 *     allowed object is
		 *     {@link TransformBean.Body.Request }
		 *     
		 */
		public void setRequest(TransformBean.Body.Request value) {
			this.request = value;
		}


		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder = {
				"identifier",
				"file",
				"physicalFile",
				"application"
		})
		public static class Request {

			@XmlElement(name = "Identifier", required = true)
			protected TransformBean.Body.Request.Identifier identifier;
			@XmlElement(name = "File", required = true)
			protected TransformBean.Body.Request.File file;
			@XmlElement(name = "PhysicalFile", required = true)
			protected TransformBean.Body.Request.PhysicalFile physicalFile;
			@XmlElement(name = "Application", required = true)
			protected TransformBean.Body.Request.Application application;

			@Override
			public String toString() {
				return "Request [identifier=" + identifier + ", file=" + file
						+ ", physicalFile=" + physicalFile + ", application="
						+ application + "]";
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result
						+ ((application == null) ? 0 : application.hashCode());
				result = prime * result
						+ ((file == null) ? 0 : file.hashCode());
				result = prime * result
						+ ((identifier == null) ? 0 : identifier.hashCode());
				result = prime
						* result
						+ ((physicalFile == null) ? 0 : physicalFile.hashCode());
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				Request other = (Request) obj;
				if (application == null) {
					if (other.application != null)
						return false;
				} else if (!application.equals(other.application))
					return false;
				if (file == null) {
					if (other.file != null)
						return false;
				} else if (!file.equals(other.file))
					return false;
				if (identifier == null) {
					if (other.identifier != null)
						return false;
				} else if (!identifier.equals(other.identifier))
					return false;
				if (physicalFile == null) {
					if (other.physicalFile != null)
						return false;
				} else if (!physicalFile.equals(other.physicalFile))
					return false;
				return true;
			}

			/**
			 * Gets the value of the identifier property.
			 * 
			 * @return
			 *     possible object is
			 *     {@link TransformBean.Body.Request.Identifier }
			 *     
			 */
			public TransformBean.Body.Request.Identifier getIdentifier() {
				return identifier;
			}

			/**
			 * Sets the value of the identifier property.
			 * 
			 * @param value
			 *     allowed object is
			 *     {@link TransformBean.Body.Request.Identifier }
			 *     
			 */
			public void setIdentifier(TransformBean.Body.Request.Identifier value) {
				this.identifier = value;
			}

			/**
			 * Gets the value of the file property.
			 * 
			 * @return
			 *     possible object is
			 *     {@link TransformBean.Body.Request.File }
			 *     
			 */
			public TransformBean.Body.Request.File getFile() {
				return file;
			}

			/**
			 * Sets the value of the file property.
			 * 
			 * @param value
			 *     allowed object is
			 *     {@link TransformBean.Body.Request.File }
			 *     
			 */
			public void setFile(TransformBean.Body.Request.File value) {
				this.file = value;
			}

			/**
			 * Gets the value of the physicalFile property.
			 * 
			 * @return
			 *     possible object is
			 *     {@link TransformBean.Body.Request.PhysicalFile }
			 *     
			 */
			public TransformBean.Body.Request.PhysicalFile getPhysicalFile() {
				return physicalFile;
			}

			/**
			 * Sets the value of the physicalFile property.
			 * 
			 * @param value
			 *     allowed object is
			 *     {@link TransformBean.Body.Request.PhysicalFile }
			 *     
			 */
			public void setPhysicalFile(TransformBean.Body.Request.PhysicalFile value) {
				this.physicalFile = value;
			}

			/**
			 * Gets the value of the application property.
			 * 
			 * @return
			 *     possible object is
			 *     {@link TransformBean.Body.Request.Application }
			 *     
			 */
			public TransformBean.Body.Request.Application getApplication() {
				return application;
			}

			/**
			 * Sets the value of the application property.
			 * 
			 * @param value
			 *     allowed object is
			 *     {@link TransformBean.Body.Request.Application }
			 *     
			 */
			public void setApplication(TransformBean.Body.Request.Application value) {
				this.application = value;
			}


			@XmlAccessorType(XmlAccessType.FIELD)
			@XmlType(name = "", propOrder = {
					"queueMgr",
					"queueNm",
					"paramTxt"
			})
			public static class Application {

				@XmlElement(name = "QueueMgr", required = true)
				protected String queueMgr;
				@XmlElement(name = "QueueNm", required = true)
				protected String queueNm;
				@XmlElement(name = "ParamTxt")
				protected List<TransformBean.Body.Request.Application.ParamTxt> paramTxt;


				@Override
				public String toString() {
					return "Application [queueMgr=" + queueMgr + ", queueNm="
							+ queueNm + ", paramTxt=" + paramTxt + "]";
				}

				@Override
				public int hashCode() {
					final int prime = 31;
					int result = 1;
					result = prime * result
							+ ((paramTxt == null) ? 0 : paramTxt.hashCode());
					result = prime * result
							+ ((queueMgr == null) ? 0 : queueMgr.hashCode());
					result = prime * result
							+ ((queueNm == null) ? 0 : queueNm.hashCode());
					return result;
				}

				@Override
				public boolean equals(Object obj) {
					if (this == obj)
						return true;
					if (obj == null)
						return false;
					if (getClass() != obj.getClass())
						return false;
					Application other = (Application) obj;
					if (paramTxt == null) {
						if (other.paramTxt != null)
							return false;
					} else if (!paramTxt.equals(other.paramTxt))
						return false;
					if (queueMgr == null) {
						if (other.queueMgr != null)
							return false;
					} else if (!queueMgr.equals(other.queueMgr))
						return false;
					if (queueNm == null) {
						if (other.queueNm != null)
							return false;
					} else if (!queueNm.equals(other.queueNm))
						return false;
					return true;
				}

				/**
				 * Gets the value of the queueMgr property.
				 * 
				 * @return
				 *     possible object is
				 *     {@link String }
				 *     
				 */
				public String getQueueMgr() {
					return queueMgr;
				}

				/**
				 * Sets the value of the queueMgr property.
				 * 
				 * @param value
				 *     allowed object is
				 *     {@link String }
				 *     
				 */
				public void setQueueMgr(String value) {
					this.queueMgr = value;
				}

				/**
				 * Gets the value of the queueNm property.
				 * 
				 * @return
				 *     possible object is
				 *     {@link String }
				 *     
				 */
				public String getQueueNm() {
					return queueNm;
				}

				/**
				 * Sets the value of the queueNm property.
				 * 
				 * @param value
				 *     allowed object is
				 *     {@link String }
				 *     
				 */
				public void setQueueNm(String value) {
					this.queueNm = value;
				}

				/**
				 * Gets the value of the paramTxt property.
				 * 
				 * <p>
				 * This accessor method returns a reference to the live list,
				 * not a snapshot. Therefore any modification you make to the
				 * returned list will be present inside the JAXB object.
				 * This is why there is not a <CODE>set</CODE> method for the paramTxt property.
				 * 
				 * <p>
				 * For example, to add a new item, do as follows:
				 * <pre>
				 *    getParamTxt().add(newItem);
				 * </pre>
				 * 
				 * 
				 * <p>
				 * Objects of the following type(s) are allowed in the list
				 * {@link TransformBean.Body.Request.Application.ParamTxt }
				 * 
				 * 
				 */
				public List<TransformBean.Body.Request.Application.ParamTxt> getParamTxt() {
					if (paramTxt == null) {
						paramTxt = new ArrayList<TransformBean.Body.Request.Application.ParamTxt>();
					}
					return this.paramTxt;
				}


				@XmlAccessorType(XmlAccessType.FIELD)
				@XmlType(name = "", propOrder = {
						"value"
				})
				public static class ParamTxt {

					@XmlValue
					protected String value;
					@XmlAttribute(name = "ParmNm")
					protected String parmNm;



					@Override
					public String toString() {
						return "ParamTxt [value=" + value + ", parmNm="
								+ parmNm + "]";
					}

					@Override
					public int hashCode() {
						final int prime = 31;
						int result = 1;

						if(! TransformParser.tagAttrToOmit.equals(parmNm)){

							result = prime * result
									+ ((parmNm == null) ? 0 : parmNm.hashCode());
							result = prime * result
									+ ((value == null) ? 0 : value.hashCode());

						}

						return result;
					}

					@Override
					public boolean equals(Object obj) {
						if (this == obj)
							return true;
						if (obj == null)
							return false;
						if (getClass() != obj.getClass())
							return false;
						ParamTxt other = (ParamTxt) obj;

						if( ! (TransformParser.tagAttrToOmit.equals(parmNm) && TransformParser.tagAttrToOmit.equals(other.parmNm)) ){
							if (parmNm == null) {
								if (other.parmNm != null)
									return false;
							} else if (!parmNm.equals(other.parmNm))
								return false;
							if (value == null) {
								if (other.value != null)
									return false;
							} else if (!value.equals(other.value))
								return false;
						}


						return true;
					}

					/**
					 * Gets the value of the value property.
					 * 
					 * @return
					 *     possible object is
					 *     {@link String }
					 *     
					 */
					public String getValue() {
						return value;
					}

					/**
					 * Sets the value of the value property.
					 * 
					 * @param value
					 *     allowed object is
					 *     {@link String }
					 *     
					 */
					public void setValue(String value) {
						this.value = value;
					}

					/**
					 * Gets the value of the parmNm property.
					 * 
					 * @return
					 *     possible object is
					 *     {@link String }
					 *     
					 */
					public String getParmNm() {
						return parmNm;
					}

					/**
					 * Sets the value of the parmNm property.
					 * 
					 * @param value
					 *     allowed object is
					 *     {@link String }
					 *     
					 */
					public void setParmNm(String value) {
						this.parmNm = value;
					}

				}

			}


			@XmlAccessorType(XmlAccessType.FIELD)
			@XmlType(name = "", propOrder = {
					"fileId",
					"baseFileNm",
					"prodInd",
					"fileTypCd"
			})
			public static class File {

				@XmlElement(name = "FileId", required = true)
				protected String fileId;
				@XmlElement(name = "BaseFileNm", required = true)
				protected String baseFileNm;
				@XmlElement(name = "ProdInd", required = true)
				protected String prodInd;
				@XmlElement(name = "FileTypCd")
				protected short fileTypCd;


				@Override
				public String toString() {
					return "File [fileId=" + fileId + ", baseFileNm="
							+ baseFileNm + ", prodInd=" + prodInd
							+ ", fileTypCd=" + fileTypCd + "]";
				}

				@Override
				public int hashCode() {
					final int prime = 31;
					int result = 1;
					result = prime
							* result
							+ ((baseFileNm == null) ? 0 : baseFileNm.hashCode());
					result = prime * result
							+ ((fileId == null) ? 0 : fileId.hashCode());
					result = prime * result + fileTypCd;
					result = prime * result
							+ ((prodInd == null) ? 0 : prodInd.hashCode());
					return result;
				}

				@Override
				public boolean equals(Object obj) {
					if (this == obj)
						return true;
					if (obj == null)
						return false;
					if (getClass() != obj.getClass())
						return false;
					File other = (File) obj;
					if (baseFileNm == null) {
						if (other.baseFileNm != null)
							return false;
					} else if (!baseFileNm.equals(other.baseFileNm))
						return false;
					if (fileId == null) {
						if (other.fileId != null)
							return false;
					} else if (!fileId.equals(other.fileId))
						return false;
					if (fileTypCd != other.fileTypCd)
						return false;
					if (prodInd == null) {
						if (other.prodInd != null)
							return false;
					} else if (!prodInd.equals(other.prodInd))
						return false;
					return true;
				}

				/**
				 * Gets the value of the fileId property.
				 * 
				 * @return
				 *     possible object is
				 *     {@link String }
				 *     
				 */
				public String getFileId() {
					return fileId;
				}

				/**
				 * Sets the value of the fileId property.
				 * 
				 * @param value
				 *     allowed object is
				 *     {@link String }
				 *     
				 */
				public void setFileId(String value) {
					this.fileId = value;
				}

				/**
				 * Gets the value of the baseFileNm property.
				 * 
				 * @return
				 *     possible object is
				 *     {@link String }
				 *     
				 */
				public String getBaseFileNm() {
					return baseFileNm;
				}

				/**
				 * Sets the value of the baseFileNm property.
				 * 
				 * @param value
				 *     allowed object is
				 *     {@link String }
				 *     
				 */
				public void setBaseFileNm(String value) {
					this.baseFileNm = value;
				}

				/**
				 * Gets the value of the prodInd property.
				 * 
				 * @return
				 *     possible object is
				 *     {@link String }
				 *     
				 */
				public String getProdInd() {
					return prodInd;
				}

				/**
				 * Sets the value of the prodInd property.
				 * 
				 * @param value
				 *     allowed object is
				 *     {@link String }
				 *     
				 */
				public void setProdInd(String value) {
					this.prodInd = value;
				}

				/**
				 * Gets the value of the fileTypCd property.
				 * 
				 */
				public short getFileTypCd() {
					return fileTypCd;
				}

				/**
				 * Sets the value of the fileTypCd property.
				 * 
				 */
				public void setFileTypCd(short value) {
					this.fileTypCd = value;
				}

			}


			@XmlAccessorType(XmlAccessType.FIELD)
			@XmlType(name = "", propOrder = {
					"srcXmitId",
					"userId"
			})
			public static class Identifier {

				@XmlElement(name = "SrcXmitId")
				protected int srcXmitId;
				@XmlElement(name = "UserId", required = true)
				protected String userId;


				@Override
				public String toString() {
					return "Identifier [srcXmitId=" + srcXmitId + ", userId="
							+ userId + "]";
				}

				@Override
				public int hashCode() {
					final int prime = 31;
					int result = 1;
					result = prime * result + srcXmitId;
					result = prime * result
							+ ((userId == null) ? 0 : userId.hashCode());
					return result;
				}

				@Override
				public boolean equals(Object obj) {
					if (this == obj)
						return true;
					if (obj == null)
						return false;
					if (getClass() != obj.getClass())
						return false;
					Identifier other = (Identifier) obj;
					if (srcXmitId != other.srcXmitId)
						return false;
					if (userId == null) {
						if (other.userId != null)
							return false;
					} else if (!userId.equals(other.userId))
						return false;
					return true;
				}

				/**
				 * Gets the value of the srcXmitId property.
				 * 
				 */
				public int getSrcXmitId() {
					return srcXmitId;
				}

				/**
				 * Sets the value of the srcXmitId property.
				 * 
				 */
				public void setSrcXmitId(int value) {
					this.srcXmitId = value;
				}

				/**
				 * Gets the value of the userId property.
				 * 
				 * @return
				 *     possible object is
				 *     {@link String }
				 *     
				 */
				public String getUserId() {
					return userId;
				}

				/**
				 * Sets the value of the userId property.
				 * 
				 * @param value
				 *     allowed object is
				 *     {@link String }
				 *     
				 */
				public void setUserId(String value) {
					this.userId = value;
				}

			}


			@XmlAccessorType(XmlAccessType.FIELD)
			@XmlType(name = "", propOrder = {
					"pathTxt",
					"fileNm"
			})
			public static class PhysicalFile {

				@XmlElement(name = "PathTxt", required = true)
				protected String pathTxt;
				@XmlElement(name = "FileNm", required = true)
				protected String fileNm;


				@Override
				public String toString() {
					return "PhysicalFile [pathTxt=" + pathTxt + ", fileNm="
							+ fileNm + "]";
				}

				@Override
				public int hashCode() {
					final int prime = 31;
					int result = 1;
					result = prime * result
							+ ((fileNm == null) ? 0 : fileNm.hashCode());
					result = prime * result
							+ ((pathTxt == null) ? 0 : pathTxt.hashCode());
					return result;
				}

				@Override
				public boolean equals(Object obj) {
					if (this == obj)
						return true;
					if (obj == null)
						return false;
					if (getClass() != obj.getClass())
						return false;
					PhysicalFile other = (PhysicalFile) obj;
					if (fileNm == null) {
						if (other.fileNm != null)
							return false;
					} else if (!fileNm.equals(other.fileNm))
						return false;
					if (pathTxt == null) {
						if (other.pathTxt != null)
							return false;
					} else if (!pathTxt.equals(other.pathTxt))
						return false;
					return true;
				}

				/**
				 * Gets the value of the pathTxt property.
				 * 
				 * @return
				 *     possible object is
				 *     {@link String }
				 *     
				 */
				public String getPathTxt() {
					return pathTxt;
				}

				/**
				 * Sets the value of the pathTxt property.
				 * 
				 * @param value
				 *     allowed object is
				 *     {@link String }
				 *     
				 */
				public void setPathTxt(String value) {
					this.pathTxt = value;
				}

				/**
				 * Gets the value of the fileNm property.
				 * 
				 * @return
				 *     possible object is
				 *     {@link String }
				 *     
				 */
				public String getFileNm() {
					return fileNm;
				}

				/**
				 * Sets the value of the fileNm property.
				 * 
				 * @param value
				 *     allowed object is
				 *     {@link String }
				 *     
				 */
				public void setFileNm(String value) {
					this.fileNm = value;
				}

			}

		}

	}


	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = {
			"transactionBlk"
	})
	public static class Header {

		@XmlElement(name = "TransactionBlk", required = true)
		protected TransformBean.Header.TransactionBlk transactionBlk;


		@Override
		public String toString() {
			return "Header [transactionBlk=" + transactionBlk + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime
					* result
					+ ((transactionBlk == null) ? 0 : transactionBlk.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Header other = (Header) obj;
			if (transactionBlk == null) {
				if (other.transactionBlk != null)
					return false;
			} else if (!transactionBlk.equals(other.transactionBlk))
				return false;
			return true;
		}

		/**
		 * Gets the value of the transactionBlk property.
		 * 
		 * @return
		 *     possible object is
		 *     {@link TransformBean.Header.TransactionBlk }
		 *     
		 */
		public TransformBean.Header.TransactionBlk getTransactionBlk() {
			return transactionBlk;
		}

		/**
		 * Sets the value of the transactionBlk property.
		 * 
		 * @param value
		 *     allowed object is
		 *     {@link TransformBean.Header.TransactionBlk }
		 *     
		 */
		public void setTransactionBlk(TransformBean.Header.TransactionBlk value) {
			this.transactionBlk = value;
		}


		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder = {
				"originatorNm",
				"serviceNm",
				"srvcMajVersionNbr",
				"srvcMinVersionNbr",
				"msgPatternTypeCd",
				"msgTypeCd",
				"msgNm",
				"msgPersistenceTypeCd",
				"msgExpireTimeQty",
				"replyToQNm",
				"replyToQMgrNm"
		})
		public static class TransactionBlk {

			@XmlElement(name = "OriginatorNm", required = true)
			protected String originatorNm;
			@XmlElement(name = "ServiceNm", required = true)
			protected String serviceNm;
			@XmlElement(name = "SrvcMajVersionNbr")
			protected byte srvcMajVersionNbr;
			@XmlElement(name = "SrvcMinVersionNbr")
			protected byte srvcMinVersionNbr;
			@XmlElement(name = "MsgPatternTypeCd", required = true)
			protected String msgPatternTypeCd;
			@XmlElement(name = "MsgTypeCd", required = true)
			protected String msgTypeCd;
			@XmlElement(name = "MsgNm", required = true)
			protected String msgNm;
			@XmlElement(name = "MsgPersistenceTypeCd", required = true)
			protected String msgPersistenceTypeCd;
			@XmlElement(name = "MsgExpireTimeQty")
			protected byte msgExpireTimeQty;
			@XmlElement(name = "ReplyToQNm", required = true)
			protected String replyToQNm;
			@XmlElement(name = "ReplyToQMgrNm", required = true)
			protected String replyToQMgrNm;
			@XmlAttribute(name = "actor")
			protected String actor;
			@XmlAttribute(name = "majorVersion")
			protected Byte majorVersion;
			@XmlAttribute(name = "minorVersion")
			protected Byte minorVersion;
			@XmlAttribute(name = "mustUnderstand")
			protected Byte mustUnderstand;
			@XmlAttribute(name = "name")
			protected String name;

			@Override
			public String toString() {
				return "TransactionBlk [originatorNm=" + originatorNm
						+ ", serviceNm=" + serviceNm + ", srvcMajVersionNbr="
						+ srvcMajVersionNbr + ", srvcMinVersionNbr="
						+ srvcMinVersionNbr + ", msgPatternTypeCd="
						+ msgPatternTypeCd + ", msgTypeCd=" + msgTypeCd
						+ ", msgNm=" + msgNm + ", msgPersistenceTypeCd="
						+ msgPersistenceTypeCd + ", msgExpireTimeQty="
						+ msgExpireTimeQty + ", replyToQNm=" + replyToQNm
						+ ", replyToQMgrNm=" + replyToQMgrNm + ", actor="
						+ actor + ", majorVersion=" + majorVersion
						+ ", minorVersion=" + minorVersion
						+ ", mustUnderstand=" + mustUnderstand + ", name="
						+ name + "]";
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result
						+ ((actor == null) ? 0 : actor.hashCode());
				result = prime
						* result
						+ ((majorVersion == null) ? 0 : majorVersion.hashCode());
				result = prime
						* result
						+ ((minorVersion == null) ? 0 : minorVersion.hashCode());
				result = prime * result + msgExpireTimeQty;
				result = prime * result
						+ ((msgNm == null) ? 0 : msgNm.hashCode());
				result = prime
						* result
						+ ((msgPatternTypeCd == null) ? 0 : msgPatternTypeCd
								.hashCode());
				result = prime
						* result
						+ ((msgPersistenceTypeCd == null) ? 0
								: msgPersistenceTypeCd.hashCode());
				result = prime * result
						+ ((msgTypeCd == null) ? 0 : msgTypeCd.hashCode());
				result = prime
						* result
						+ ((mustUnderstand == null) ? 0 : mustUnderstand
								.hashCode());
				result = prime * result
						+ ((name == null) ? 0 : name.hashCode());
				result = prime
						* result
						+ ((originatorNm == null) ? 0 : originatorNm.hashCode());
				result = prime
						* result
						+ ((replyToQMgrNm == null) ? 0 : replyToQMgrNm
								.hashCode());
				result = prime * result
						+ ((replyToQNm == null) ? 0 : replyToQNm.hashCode());
				result = prime * result
						+ ((serviceNm == null) ? 0 : serviceNm.hashCode());
				result = prime * result + srvcMajVersionNbr;
				result = prime * result + srvcMinVersionNbr;
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				TransactionBlk other = (TransactionBlk) obj;
				if (actor == null) {
					if (other.actor != null)
						return false;
				} else if (!actor.equals(other.actor))
					return false;
				if (majorVersion == null) {
					if (other.majorVersion != null)
						return false;
				} else if (!majorVersion.equals(other.majorVersion))
					return false;
				if (minorVersion == null) {
					if (other.minorVersion != null)
						return false;
				} else if (!minorVersion.equals(other.minorVersion))
					return false;
				if (msgExpireTimeQty != other.msgExpireTimeQty)
					return false;
				if (msgNm == null) {
					if (other.msgNm != null)
						return false;
				} else if (!msgNm.equals(other.msgNm))
					return false;
				if (msgPatternTypeCd == null) {
					if (other.msgPatternTypeCd != null)
						return false;
				} else if (!msgPatternTypeCd.equals(other.msgPatternTypeCd))
					return false;
				if (msgPersistenceTypeCd == null) {
					if (other.msgPersistenceTypeCd != null)
						return false;
				} else if (!msgPersistenceTypeCd
						.equals(other.msgPersistenceTypeCd))
					return false;
				if (msgTypeCd == null) {
					if (other.msgTypeCd != null)
						return false;
				} else if (!msgTypeCd.equals(other.msgTypeCd))
					return false;
				if (mustUnderstand == null) {
					if (other.mustUnderstand != null)
						return false;
				} else if (!mustUnderstand.equals(other.mustUnderstand))
					return false;
				if (name == null) {
					if (other.name != null)
						return false;
				} else if (!name.equals(other.name))
					return false;
				if (originatorNm == null) {
					if (other.originatorNm != null)
						return false;
				} else if (!originatorNm.equals(other.originatorNm))
					return false;
				if (replyToQMgrNm == null) {
					if (other.replyToQMgrNm != null)
						return false;
				} else if (!replyToQMgrNm.equals(other.replyToQMgrNm))
					return false;
				if (replyToQNm == null) {
					if (other.replyToQNm != null)
						return false;
				} else if (!replyToQNm.equals(other.replyToQNm))
					return false;
				if (serviceNm == null) {
					if (other.serviceNm != null)
						return false;
				} else if (!serviceNm.equals(other.serviceNm))
					return false;
				if (srvcMajVersionNbr != other.srvcMajVersionNbr)
					return false;
				if (srvcMinVersionNbr != other.srvcMinVersionNbr)
					return false;
				return true;
			}

			/**
			 * Gets the value of the originatorNm property.
			 * 
			 * @return
			 *     possible object is
			 *     {@link String }
			 *     
			 */
			public String getOriginatorNm() {
				return originatorNm;
			}

			/**
			 * Sets the value of the originatorNm property.
			 * 
			 * @param value
			 *     allowed object is
			 *     {@link String }
			 *     
			 */
			public void setOriginatorNm(String value) {
				this.originatorNm = value;
			}

			/**
			 * Gets the value of the serviceNm property.
			 * 
			 * @return
			 *     possible object is
			 *     {@link String }
			 *     
			 */
			public String getServiceNm() {
				return serviceNm;
			}

			/**
			 * Sets the value of the serviceNm property.
			 * 
			 * @param value
			 *     allowed object is
			 *     {@link String }
			 *     
			 */
			public void setServiceNm(String value) {
				this.serviceNm = value;
			}

			/**
			 * Gets the value of the srvcMajVersionNbr property.
			 * 
			 */
			public byte getSrvcMajVersionNbr() {
				return srvcMajVersionNbr;
			}

			/**
			 * Sets the value of the srvcMajVersionNbr property.
			 * 
			 */
			public void setSrvcMajVersionNbr(byte value) {
				this.srvcMajVersionNbr = value;
			}

			/**
			 * Gets the value of the srvcMinVersionNbr property.
			 * 
			 */
			public byte getSrvcMinVersionNbr() {
				return srvcMinVersionNbr;
			}

			/**
			 * Sets the value of the srvcMinVersionNbr property.
			 * 
			 */
			public void setSrvcMinVersionNbr(byte value) {
				this.srvcMinVersionNbr = value;
			}

			/**
			 * Gets the value of the msgPatternTypeCd property.
			 * 
			 * @return
			 *     possible object is
			 *     {@link String }
			 *     
			 */
			public String getMsgPatternTypeCd() {
				return msgPatternTypeCd;
			}

			/**
			 * Sets the value of the msgPatternTypeCd property.
			 * 
			 * @param value
			 *     allowed object is
			 *     {@link String }
			 *     
			 */
			public void setMsgPatternTypeCd(String value) {
				this.msgPatternTypeCd = value;
			}

			/**
			 * Gets the value of the msgTypeCd property.
			 * 
			 * @return
			 *     possible object is
			 *     {@link String }
			 *     
			 */
			public String getMsgTypeCd() {
				return msgTypeCd;
			}

			/**
			 * Sets the value of the msgTypeCd property.
			 * 
			 * @param value
			 *     allowed object is
			 *     {@link String }
			 *     
			 */
			public void setMsgTypeCd(String value) {
				this.msgTypeCd = value;
			}

			/**
			 * Gets the value of the msgNm property.
			 * 
			 * @return
			 *     possible object is
			 *     {@link String }
			 *     
			 */
			public String getMsgNm() {
				return msgNm;
			}

			/**
			 * Sets the value of the msgNm property.
			 * 
			 * @param value
			 *     allowed object is
			 *     {@link String }
			 *     
			 */
			public void setMsgNm(String value) {
				this.msgNm = value;
			}

			/**
			 * Gets the value of the msgPersistenceTypeCd property.
			 * 
			 * @return
			 *     possible object is
			 *     {@link String }
			 *     
			 */
			public String getMsgPersistenceTypeCd() {
				return msgPersistenceTypeCd;
			}

			/**
			 * Sets the value of the msgPersistenceTypeCd property.
			 * 
			 * @param value
			 *     allowed object is
			 *     {@link String }
			 *     
			 */
			public void setMsgPersistenceTypeCd(String value) {
				this.msgPersistenceTypeCd = value;
			}

			/**
			 * Gets the value of the msgExpireTimeQty property.
			 * 
			 */
			public byte getMsgExpireTimeQty() {
				return msgExpireTimeQty;
			}

			/**
			 * Sets the value of the msgExpireTimeQty property.
			 * 
			 */
			public void setMsgExpireTimeQty(byte value) {
				this.msgExpireTimeQty = value;
			}

			/**
			 * Gets the value of the replyToQNm property.
			 * 
			 * @return
			 *     possible object is
			 *     {@link String }
			 *     
			 */
			public String getReplyToQNm() {
				return replyToQNm;
			}

			/**
			 * Sets the value of the replyToQNm property.
			 * 
			 * @param value
			 *     allowed object is
			 *     {@link String }
			 *     
			 */
			public void setReplyToQNm(String value) {
				this.replyToQNm = value;
			}

			/**
			 * Gets the value of the replyToQMgrNm property.
			 * 
			 * @return
			 *     possible object is
			 *     {@link String }
			 *     
			 */
			public String getReplyToQMgrNm() {
				return replyToQMgrNm;
			}

			/**
			 * Sets the value of the replyToQMgrNm property.
			 * 
			 * @param value
			 *     allowed object is
			 *     {@link String }
			 *     
			 */
			public void setReplyToQMgrNm(String value) {
				this.replyToQMgrNm = value;
			}

			/**
			 * Gets the value of the actor property.
			 * 
			 * @return
			 *     possible object is
			 *     {@link String }
			 *     
			 */
			public String getActor() {
				return actor;
			}

			/**
			 * Sets the value of the actor property.
			 * 
			 * @param value
			 *     allowed object is
			 *     {@link String }
			 *     
			 */
			public void setActor(String value) {
				this.actor = value;
			}

			/**
			 * Gets the value of the majorVersion property.
			 * 
			 * @return
			 *     possible object is
			 *     {@link Byte }
			 *     
			 */
			public Byte getMajorVersion() {
				return majorVersion;
			}

			/**
			 * Sets the value of the majorVersion property.
			 * 
			 * @param value
			 *     allowed object is
			 *     {@link Byte }
			 *     
			 */
			public void setMajorVersion(Byte value) {
				this.majorVersion = value;
			}

			/**
			 * Gets the value of the minorVersion property.
			 * 
			 * @return
			 *     possible object is
			 *     {@link Byte }
			 *     
			 */
			public Byte getMinorVersion() {
				return minorVersion;
			}

			/**
			 * Sets the value of the minorVersion property.
			 * 
			 * @param value
			 *     allowed object is
			 *     {@link Byte }
			 *     
			 */
			public void setMinorVersion(Byte value) {
				this.minorVersion = value;
			}

			/**
			 * Gets the value of the mustUnderstand property.
			 * 
			 * @return
			 *     possible object is
			 *     {@link Byte }
			 *     
			 */
			public Byte getMustUnderstand() {
				return mustUnderstand;
			}

			/**
			 * Sets the value of the mustUnderstand property.
			 * 
			 * @param value
			 *     allowed object is
			 *     {@link Byte }
			 *     
			 */
			public void setMustUnderstand(Byte value) {
				this.mustUnderstand = value;
			}

			/**
			 * Gets the value of the name property.
			 * 
			 * @return
			 *     possible object is
			 *     {@link String }
			 *     
			 */
			public String getName() {
				return name;
			}

			/**
			 * Sets the value of the name property.
			 * 
			 * @param value
			 *     allowed object is
			 *     {@link String }
			 *     
			 */
			public void setName(String value) {
				this.name = value;
			}

		}

	}

}
